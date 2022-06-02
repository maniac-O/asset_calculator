package com.SH.asset_calculator.domain;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
public class Field {

    @GeneratedValue @Id
    private Long id;
    private String uid;
    private String fieldName;
    private String value;

    public Field(String uid, String fieldName, String value) {
        this.uid = uid;
        this.fieldName = fieldName;
        this.value = value;
    }

    // todo 시간 넣기
    // latest, 회원가입시간,
}
