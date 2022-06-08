package com.SH.asset_calculator.controller;

import com.SH.asset_calculator.domain.Board;
import com.SH.asset_calculator.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/openboard")
    public String openBoard(Model model) {
        ArrayList<Board> boards = boardService.getBoard();
        model.addAttribute("boards", boards);
        model.addAttribute("lastIndex",boards.get(boards.size() - 1).getIndex());

        return "openboard";
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
