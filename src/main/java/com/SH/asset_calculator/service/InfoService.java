package com.SH.asset_calculator.service;

import com.SH.asset_calculator.converter.Converter;
import com.SH.asset_calculator.domain.ConfigModel;
import com.SH.asset_calculator.domain.FirebaseCollectionEnum;
import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.exception.BadArgumentException;
import com.SH.asset_calculator.repository.InfoRepository;
import com.SH.asset_calculator.valid.ValidSetupPrice;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.SH.asset_calculator.domain.FirebaseCollectionEnum.FIELDNAME;
import static com.SH.asset_calculator.domain.FirebaseCollectionEnum.PRICE_HISTORY;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;
    private final ValidSetupPrice validSetupPrice;
    private final Converter converter;

    public Boolean setInfo(Member member, Map<String, String> dataSet) throws BadArgumentException {

        HashMap<String, HashMap> param = new HashMap<>();
        HashMap<String, String> smallParam = new HashMap<>();

        if (dataSet.isEmpty()) {
            log.warn("From Config, Empty Argument" + member.getUid());
            return false;
        }

        for (String key : dataSet.keySet()) {
            if (dataSet.get(key).isEmpty()) {
                log.warn("Bad Argu");
                return false;
            }
            smallParam.put(key, dataSet.get(key));
        }
        param.put("config_value", smallParam);

        infoRepository.setConfig(member, param);

        // 공개범위 변경시 데이터 올리기
        infoRepository.setScope(member, smallParam.get("scope"));

        return true;
    }

    public ConfigModel getInfo(Member member) {
        DocumentSnapshot result = infoRepository.getConfig(member);

        if (result.get("config_value") == null) {
            return null;
        }

        HashMap<String, String> hashMap = converter.stringToMap(result.get("config_value").toString());

        return new ConfigModel(hashMap.get("scope"));
    }



    public Boolean setField(Member member, Map dataSet) throws ExecutionException, InterruptedException, BadArgumentException {

        HashMap<String, HashMap<String, HashMap>> params = parsingParam(parseInput(member, dataSet));
        Integer index = infoRepository.getIndex()+1;

        boolean result = infoRepository.setField(FIELDNAME.label(), member, params, index);

        ConfigModel info = getInfo(member);
        if (info.getPublic_scope().equals("true")) {
            result = infoRepository.setScope(member, info.getPublic_scope());
        }

        return result;
    }

    public HashMap<String, HashMap<String, String[]>> getField(Member member) {
        // get data from Firebase
        DocumentSnapshot field = infoRepository.getField(member);
        infoRepository.getIndex();

        return converter.stringToFieldForm(field.getData().toString());
    }

    // setupprice 페이지에서 넘겨준 데이터를 infoRepository에 전달해주기 전 데이터 변형
    private HashMap<String, HashMap<String, HashMap>> parsingParam(ArrayList params){

        HashMap<String, HashMap<String, HashMap>> param = new HashMap<>();
        HashMap<String, HashMap> p1 = new HashMap<>();
        HashMap<String, HashMap> p2 = new HashMap<>();

        p1.put("manual_field", (HashMap) params.get(0));
        p1.put("custom_field",(HashMap) params.get(1));
        p2.put("manual_percent", (HashMap) params.get(2));
        p2.put("custom_percent", (HashMap) params.get(3));
        param.put("finance", p1);
        param.put("percent", p2);

        return param;
    }
    public ArrayList parseInput(Member member, Map dataSet) throws BadArgumentException {
        JSONParser parser = new JSONParser(dataSet.get("list").toString());
        ArrayList<Object> dataArr = null;
        try {
            dataArr = parser.parseArray();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
        if (dataArr.get(1) != null) {
            Iterator<String> keys = ((LinkedHashMap) dataArr.get(1)).keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                ArrayList<String> tmp = (ArrayList<String>) ((LinkedHashMap) dataArr.get(1)).get(key);

                if (tmp.get(0).equals("은행")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "BANKACCOUNT");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap) dataArr.get(1)).put(key, replace_element);
                } else if (tmp.get(0).equals("거래소")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "MARKET");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap) dataArr.get(1)).put(key, replace_element);
                } else if (tmp.get(0).equals("기타")) {
                    ArrayList<String> replace_element = new ArrayList<>();
                    replace_element.add(0, "ETC");
                    replace_element.add(1, tmp.get(1));

                    ((LinkedHashMap) dataArr.get(1)).put(key, replace_element);
                }

            }
        }

         */

        log.info("Parsing Data is 'First: " + dataArr.get(0).toString() + "' " +
                "'Second: " + dataArr.get(1) + "'");


        if (((HashMap) dataArr.get(0)).containsValue(""))
            throw new BadArgumentException("BadArug");

        if (!validSetupPrice.valid(member, dataArr))
            throw new BadArgumentException("custom field's value is bad argu");

        return dataArr;
    }
}
