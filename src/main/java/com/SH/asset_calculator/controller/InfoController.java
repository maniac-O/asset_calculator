package com.SH.asset_calculator.controller;

import com.SH.asset_calculator.domain.*;
import com.SH.asset_calculator.exception.BadArgumentException;
import com.SH.asset_calculator.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;
    private final MemberFromSession memberFromSession;

    @GetMapping("/config")
    public String config(Model model, HttpServletRequest request){
        Member member = memberFromSession.convert(request);
        ConfigModel config = infoService.getInfo(member);

        model.addAttribute("config", config);

        if (config == null) {
            model.addAttribute("config", new ConfigModel(""));
        }

        return "config";
    }

    @PostMapping("/config")
    public String setConfig(@RequestParam Map dataSet, HttpServletRequest request) throws BadArgumentException {
        Member member = memberFromSession.convert(request);
        infoService.setInfo(member, dataSet);
        return "redirect:/";
    }


    @GetMapping("/getInfo")
    public String getInfo(Model model, HttpServletRequest request) {
        Member member = memberFromSession.convert(request);

        HashMap<String, HashMap<String, String[]>> field = infoService.getField(member);

        if (field.isEmpty()) {
            field.put("custom_field", null);
            field.put("manual_field", null);
        }

        model.addAttribute("field", field);


        return "getInfo";
    }

    @GetMapping("/setupprice")
    public String setupPrice(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("accessToken") == null) {
            model.addAttribute("member", new Member());
            return "home";
        }

        List<String> fieldGroup = Stream.of(FieldGroup.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        model.addAttribute("fieldGroup", fieldGroup);
        return "setupprice";
    }

    @PostMapping("/setupprice")
    public String setupPrice_response(@RequestParam Map dataSet, Model model, HttpServletRequest request) throws ParseException, BadArgumentException {

        Member member = memberFromSession.convert(request);

        if (member == null)
            return "index";
        model.addAttribute("member", member);

        // 모든 검증 이후 saveField()를 호출함
        try {
            infoService.setField(member, dataSet);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "home";
    }

}
