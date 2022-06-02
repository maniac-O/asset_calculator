package com.SH.asset_calculator.domain;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class MemberFromSession {

    public Member convert(HttpServletRequest request){
        HttpSession session = request.getSession();

        if (session.getAttribute("accessToken") == null) {
            return null;
        }

        return new Member(session.getAttribute("uid").toString(), session.getAttribute("accessToken").toString(),
                session.getAttribute("email").toString(), session.getAttribute("displayName").toString());


    }
}
