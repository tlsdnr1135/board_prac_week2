package com.week2.magazine.board.dto;

import com.week2.magazine.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BoardResponseDto {

    private Long userId;

    private String nickname;
    private String content;
    private String img_Url;

    private List<LikesDto> likes;
    private int layoutType;


    public BoardResponseDto(Board board) {
        this.userId = board.getAccount().getId();
        this.nickname = board.getAccount().getNickname();
        this.content = board.getContent();
        this.img_Url = board.getImg_url();
        this.likes = board.getLikes().stream().map(LikesDto::new).collect(Collectors.toList());
        this.layoutType = board.getLayoutType();
    }

}
