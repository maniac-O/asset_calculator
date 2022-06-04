package com.SH.asset_calculator.domain;

import lombok.Getter;

@Getter
public class ConfigModel {

    private String public_scope;

    public ConfigModel(String public_scope) {
        this.public_scope = public_scope;
    }
}
