package com.week2.magazine.account.dto;

import com.week2.magazine.account.Account;
import com.week2.magazine.account.AccountController;
import com.week2.magazine.account.AccountRoleEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Data
public class AccountDto {

//    private final AccountController accountControllerl;

    @Email
    @NotBlank
//    @Length(min = 3, max =30)
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    @Pattern(regexp = "^[A-Za-z0-9@.]{3,20}$")
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    @Length(min=4, max = 20)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private AccountRoleEnum role;

    public Account toEntity(){
        Account account = Account.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .role(role)
                .build();
//        account.encodePassword(passwordEncoder);
        return account;
    }

}
