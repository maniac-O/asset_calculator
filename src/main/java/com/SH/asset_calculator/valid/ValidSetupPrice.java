package com.SH.asset_calculator.valid;

import com.SH.asset_calculator.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class ValidSetupPrice {

    public boolean valid(Member member, ArrayList<Object> dataSet){
        if (dataSet.size() < 4)
            return false;

        for (int i = 0; i < 4; i++) {
            if (dataSet.get(i) == null)
                return false;
        }

        HashMap<String, String> baseMap = (HashMap) dataSet.get(0);
        HashMap<String, ArrayList<String>> validMap = (HashMap) dataSet.get(1);
        HashMap<String,Integer> validValue = new HashMap<>();

        for (String key :validMap.keySet() ) {
            validValue.put(validMap.get(key).get(0), 0);
        }


        for (String key :validMap.keySet() ) {
            validValue.put(validMap.get(key).get(0), validValue.get(validMap.get(key).get(0))+Integer.parseInt(validMap.get(key).get(1)));

            if (Integer.parseInt(baseMap.get(validMap.get(key).get(0))) < validValue.get(validMap.get(key).get(0))){
                log.warn("Valid is Failed 'UID: "+member.getUid()+"'");
                return false;
            }
        }



        return true;
    }
}
