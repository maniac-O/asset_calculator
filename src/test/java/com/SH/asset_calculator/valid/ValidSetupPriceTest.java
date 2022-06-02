package com.SH.asset_calculator.valid;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidSetupPriceTest {

    @Test
    public boolean valid(HashMap<String, String> baseMap, HashMap<String, ArrayList<String>> validMap){

        for (String key :validMap.keySet() ) {
            System.out.println("validMap = " + validMap.get(key));
        }


        return true;
    }
}