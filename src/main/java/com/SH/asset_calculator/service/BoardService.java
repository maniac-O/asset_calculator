package com.SH.asset_calculator.service;

import com.SH.asset_calculator.converter.Converter;
import com.SH.asset_calculator.domain.Board;
import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.repository.BoardRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final Converter converter;


    public ArrayList<Board> getBoard(){

        List<QueryDocumentSnapshot> result = boardRepository.getBoard();
        return boardsToArrayList(result);
    }

    public ArrayList<Board> getLastBoard(String index) {
        List<QueryDocumentSnapshot> result = boardRepository.getMoreBoard(index);
        System.out.println("result.get() = " + result.get(0).getId());
        return boardsToArrayList(result);
    }

    private ArrayList<Board> boardsToArrayList(List<QueryDocumentSnapshot> value) {
        if ( value == null){
            return null;
        }

        ArrayList<Board> boards = new ArrayList<>();
        for (QueryDocumentSnapshot item: value ){
            String data = converter.stringToMap(item.get("data").toString()).toString();

            HashMap<String, HashMap<String, String[]>> param = converter.stringToFieldForm(data);

            // todo: item.getId() --> item.get("id").toString() 으로 변경해줘야함
            Board board = new Board(Timestamp.valueOf(item.get("createTime").toString().replace("T"," ").split("\\.")[0]),
                    Timestamp.valueOf(item.get("updateTime").toString().replace("T"," ").split("\\.")[0]),
                    item.get("displayName").toString(), item.getId(), item.get("reference").toString(), Integer.parseInt(item.get("index").toString()), param);
            boards.add(board);
        }

        return boards;
    }
}
