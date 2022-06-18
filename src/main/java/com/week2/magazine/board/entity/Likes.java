package com.week2.magazine.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.week2.magazine.account.Account;
import com.week2.magazine.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="AccountId")
    private Account account;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="BoardId")
    private Board board;


}
