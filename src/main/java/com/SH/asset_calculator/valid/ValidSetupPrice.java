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

    public boolean valid(Member member, HashMap<String, String> baseMap, HashMap<String, ArrayList<String>> validMap){
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
