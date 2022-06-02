package com.SH.asset_calculator.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {
    @Id
    private String uid;
    private String accessToken;
    private String email;
    private String displayName;

    public Member() {}

    public Member(String uid, String accessToken, String email, String displayName) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.email = email;
        this.displayName = displayName;
    }

}
