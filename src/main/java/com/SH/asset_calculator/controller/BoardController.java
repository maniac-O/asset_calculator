package com.SH.asset_calculator.controller;

import com.SH.asset_calculator.domain.Board;
import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.domain.MemberFromSession;
import com.SH.asset_calculator.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberFromSession memberFromSession;

    @GetMapping("/openboard")
    public String openBoard(Model model) {
        ArrayList<Board> boards = boardService.getBoard();
        model.addAttribute("boards", boards);
        model.addAttribute("lastIndex",boards.get(boards.size() - 1).getIndex());

        return "openboard";
    }

/*
    @GetMapping("/openboard")
    public String openBoard(Model model, HttpServletRequest request) {
        Member member = memberFromSession.convert(request);
        ArrayList<Board> boards = boardService.getBoard(member);
        model.addAttribute("boards", boards);
        model.addAttribute("lastIndex",boards.get(boards.size() - 1).getIndex());

        return "openboard";
    }
 */

    @GetMapping("/openboard/{id}")
    public String userHistory(@PathVariable("id") String uid, Model model){
        System.out.println("uid = " + uid);
        

        return "userhistory";
    }

    @ResponseBody
    @GetMapping("/morepage")
    public ArrayList<Board> morePage(String lastIndex){
        System.out.println("index = " + lastIndex);

        if (lastIndex ==null)
            return null;

        ArrayList<Board> result = boardService.getLastBoard(lastIndex);
        return result;
    }
}
