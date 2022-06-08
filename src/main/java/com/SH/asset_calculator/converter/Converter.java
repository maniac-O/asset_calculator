package com.SH.asset_calculator.converter;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class Converter {

    public String[] stringToArray(String value) {
        String[] chars = {"[", "]"};

        for ( String ch: chars)
            value = value.replace(ch, "");

        return value.split(",");
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

    public HashMap<String, HashMap<String, String[]>> stringToFieldForm(String param) {

        // init variable
        HashMap<String, String[]> value_manual = new HashMap<>();
        HashMap<String, String[]> value_custom = new HashMap<>();
        HashMap<String, String[]> percent_manual = new HashMap<>();
        HashMap<String, String[]> percent_custom = new HashMap<>();
        HashMap<String, HashMap<String, String[]>> result = new HashMap<>();

        HashMap<String, String> field = stringToMap(param);


        // convert String to Map ## (manual_field)
        HashMap<String, String> finance = stringToMap(field.get("finance").toString());
        HashMap<String, String> manual_field = stringToMap(finance.get("manual_field"));

        // manual_field를 result의 형식에 맞추기 위한 부분
        for (String key : manual_field.keySet()) {
            String[] s = {"tmp"};
            s[0] = manual_field.get(key);
            value_manual.put(key, s);
        }
        result.put("manual_field", value_manual);
        // end


        // start parsing convert String to Map ## (custom_field)
        HashMap<String, String> custom_field = stringToMap(finance.get("custom_field").toString());

        if (custom_field == null) {
            result.put("custom_field", null);
            return result;
        }

        for (String key : custom_field.keySet()) {
            value_custom.put(key, stringToArray(custom_field.get(key)));
        }
        result.put("custom_field", value_custom);
        // end



        // parsing percent start
        HashMap<String, String> percent = stringToMap(field.get("percent").toString());
        HashMap<String, String> manual_percent = stringToMap(percent.get("manual_percent"));

        // manual_field를 result의 형식에 맞추기 위한 부분
        for (String key : manual_percent.keySet()) {
            String[] s = {"tmp"};
            s[0] = manual_percent.get(key);
            percent_manual.put(key, s);
        }
        result.put("manual_percent", percent_manual);
        // end


        // start parsing convert String to Map ## (custom_field)
        HashMap<String, String> custom_percent = stringToMap(percent.get("custom_percent").toString());

        if (custom_percent == null) {
            result.put("custom_percent", null);
            return result;
        }

        for (String key : custom_percent.keySet()) {
            percent_custom.put(key, stringToArray(custom_percent.get(key)));
        }
        result.put("custom_percent", percent_custom);
        // end

        return result;
    }
}
