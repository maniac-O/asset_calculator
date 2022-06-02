package com.SH.asset_calculator.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFieldValues {

    private String fieldName;
    private String value;

    public CustomFieldValues(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }
}
