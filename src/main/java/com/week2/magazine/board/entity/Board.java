package com.week2.magazine.board.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.week2.magazine.account.Account;
import com.week2.magazine.board.TimeStamped;
import com.week2.magazine.board.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Board extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int layoutType;

    private String content;

    private String img_url;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="AccountId")
    private Account account;


    @JsonManagedReference
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();


    //Board 함수
    public void boardUpdate(BoardDto boardDto,List<String> list){
        this.content = boardDto.getContent();
        this.img_url = list.get(0);
        this.layoutType = boardDto.getLayoutType();
    }



}
