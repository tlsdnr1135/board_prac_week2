package com.week2.magazine.board.dto;

import com.week2.magazine.board.entity.Likes;
import lombok.Data;

@Data
public class LikesDto {

    private Long userId;
    public LikesDto(Likes likes) {
        this.userId = likes.getAccount().getId();
    }
}
