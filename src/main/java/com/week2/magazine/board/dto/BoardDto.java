package com.week2.magazine.board.dto;

import com.week2.magazine.account.Account;
import com.week2.magazine.board.entity.Board;
import com.week2.magazine.board.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {

    private Long userId;

    private String userEmail;
    private String content;
    private String img_Url;

    private List<Likes> likes;
    private int layoutType;

    //게시글 작성
    public Board addBoardList(Account account){
        return Board.builder()
                .account(account)
                .content(content)
                .img_url(img_Url)
                .layoutType(layoutType)
                .build();
    }

}