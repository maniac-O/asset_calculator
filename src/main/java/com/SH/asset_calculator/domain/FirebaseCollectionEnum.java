package com.SH.asset_calculator.domain;

public enum FirebaseCollectionEnum {
    FIELDNAME ("price_latest"),
    CONFIGNAME ("configuration"),
    PUBLIC_SCOPE ("public_scope"),
    PRICE_HISTORY ("price_history");

    private final String label;

    FirebaseCollectionEnum(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
