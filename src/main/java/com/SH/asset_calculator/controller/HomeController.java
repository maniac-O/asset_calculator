package com.SH.asset_calculator.controller;

import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.domain.MemberFromSession;
import com.SH.asset_calculator.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberFromSession memberFromSession;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        model.addAttribute("member", new Member("","ANONYMOUS","",""));

        if (session != null && session.getAttribute("accessToken") != null) {
            Member member = memberFromSession.convert(request);
            model.addAttribute("member", member);
        }


        return "home";
    }

    @PostMapping("/")
    public String home2() {
        return "logout";
    }
}
