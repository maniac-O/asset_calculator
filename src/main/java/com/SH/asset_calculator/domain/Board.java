package com.SH.asset_calculator.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

@Getter
@Setter
public class Board {
    private Timestamp createTime;
    private Timestamp updateTime;

    private String displayName;
    private String id;
    private String reference;
    private Integer index;

    private HashMap<String, HashMap<String, String[]>> data;

    public Board(Timestamp createTime, Timestamp updateTime, String displayName, String id, String reference, Integer index, HashMap<String, HashMap<String, String[]>> data) {
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.displayName = displayName;
        this.id = id;
        this.reference = reference;
        this.index = index;
        this.data = data;
    }
}
