package com.week2.magazine.account;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.week2.magazine.board.entity.Board;
import com.week2.magazine.board.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

//TODO react랑 연동할 때 csrf안쓰나????
//TODO 빌더 패턴에서 아래 두개 어노테이션이 필요한 이유
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Email
    @Column(unique = true)
    private String email;
    private String password;

    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountRoleEnum role;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Board> board =new ArrayList<>();

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

}
