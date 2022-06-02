package com.SH.asset_calculator.controller;

import com.SH.asset_calculator.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {
    private final static String UID = "uid";
    private final static String ACCESSTOKEN = "accessToken";
    private final static String EMAIL = "email";
    private final static String DISPLAYNAME = "displayName";

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("member", new Member());

        return "login";
    }

    @PostMapping("/login")
    public String loginValid(Member member, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute(UID, member.getUid());
        session.setAttribute(ACCESSTOKEN, member.getAccessToken());
        session.setAttribute(EMAIL, member.getEmail());
        session.setAttribute(DISPLAYNAME, member.getDisplayName());

        log.info("User try to Login 'USER NAME: "+member.getDisplayName()+
                "' 'ACCESS TOKEN: " + member.getAccessToken() +"'");

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(ACCESSTOKEN) != null) {
            session.invalidate();
        }

        return "logout";
    }
}
