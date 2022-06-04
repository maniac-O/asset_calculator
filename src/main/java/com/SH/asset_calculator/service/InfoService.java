package com.SH.asset_calculator.service;

import com.SH.asset_calculator.domain.ConfigModel;
import com.SH.asset_calculator.domain.Field;
import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.exception.BadArgumentException;
import com.SH.asset_calculator.repository.InfoRepository;
import com.SH.asset_calculator.valid.ValidSetupPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;
    private final ValidSetupPrice validSetupPrice;

    public Boolean setInfo(Member member, Map<String, String> dataSet) throws BadArgumentException {

        HashMap<String, HashMap> param = new HashMap<>();
        HashMap<String, String> smallParam = new HashMap<>();

        System.out.println("dataSet = " + dataSet);

        if (dataSet.isEmpty()) {
            log.warn("From Config, Empty Argument"+member.getUid());
            return false;
        }

        for (String key : dataSet.keySet()) {
            System.out.println("dataSet.Key = " + dataSet.get(key)+member.getUid());
            if (dataSet.get(key).isEmpty()) {
                log.warn("Bad Argu");
                return false;
            }
            smallParam.put(key, dataSet.get(key));
        }
        param.put("config_value",smallParam);

        infoRepository.setConfig(member, param);

        return true;
    }

    public ConfigModel getInfo(Member member) {
        DocumentSnapshot result = infoRepository.getConfig(member);

        if (result.get("config_value") == null) {
            return null;
        }

        HashMap<String, String> hashMap = stringToMap(result.get("config_value").toString());

        return new ConfigModel(hashMap.get("scope"));
    }

    public ArrayList parseInput(Member member, Map dataSet) throws BadArgumentException {
        JSONParser parser = new JSONParser(dataSet.get("list").toString());
        ArrayList<Object> dataArr = null;
        try {
            dataArr = parser.parseArray();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dataArr.get(1) != null) {
            Iterator<String> keys = ((LinkedHashMap)dataArr.get(1)).keySet().iterator();
            while (keys.hasNext()){
                String key = keys.next();
                ArrayList<String> tmp = (ArrayList<String>) ((LinkedHashMap)dataArr.get(1)).get(key);

                if (tmp.get(0).equals("은행")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "BANKACCOUNT");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap)dataArr.get(1)).put(key, replace_element);
                }else if (tmp.get(0).equals("거래소")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "MARKET");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap)dataArr.get(1)).put(key, replace_element);
                }else if (tmp.get(0).equals("기타")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "ETC");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap)dataArr.get(1)).put(key, replace_element);
                }

            }
        }

        log.info("Parsing Data is 'First: "+dataArr.get(0).toString()+"' "+
                "'Second: "+dataArr.get(1)+"'");


        if(((HashMap)dataArr.get(0)).containsValue(""))
            throw new BadArgumentException("BadArug");

        if (!validSetupPrice.valid(member, ((HashMap) dataArr.get(0)), ((HashMap) dataArr.get(1))))
            throw new BadArgumentException("custom field's value is bad argu");

        return dataArr;
    }

    public Boolean saveField(String uid, HashMap<String, String> manual_fields,HashMap<String, ArrayList<String>> custom_fields) throws ExecutionException, InterruptedException {

        return infoRepository.insertField(uid, manual_fields, custom_fields);
    }

    public HashMap<String, ArrayList<Field>> getField(Member member){
        // init variable
        ArrayList<Field> value_manual = new ArrayList<>();
        ArrayList<Field> value_custom = new ArrayList<>();
        HashMap<String, ArrayList<Field>> result = new HashMap<>();

        // get data from Firebase
        DocumentSnapshot field = infoRepository.getField(member);

        // convert String to Map ## (manual_field)
        HashMap<String, String> params = stringToMap(field.get("manual_field").toString());

        for (String key:params.keySet()) {
            value_manual.add(new Field(member.getUid(), key, params.get(key)));
        }
        result.put("manual_field", value_manual);
        // end


        // start parsing convert String to Map ## (custom_field)
        params = stringToMap(field.get("custom_field").toString());

        if (params == null)
            return result;

        for (String key:params.keySet()) {
            value_custom.add(new Field(member.getUid(), key, params.get(key)));
        }
        result.put("custom_field", value_custom);
        // end


        return result;
    }

    public HashMap<String, String> stringToMap(String argu){
        argu = argu.replace('=', ':');
        JSONObject json = new JSONObject(argu);

        if (json.isEmpty())
            return null;

        HashMap<String, String> hashMap = new HashMap<>();

        Iterator<String> keys = json.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            hashMap.put(key, json.get(key).toString());
        }

        return hashMap;
    }

    public String[] parsingCustomField(String custom_field) {
        // split code
        String[] chars = {"[", "]", "\""};

        for (int i = 0; i < chars.length; i++) {
            custom_field = custom_field.replace(chars[i], "");
        }
        String[] custom_values = custom_field.split(",");

        return custom_values;
    }
}
