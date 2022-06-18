package com.week2.magazine.account;

import com.week2.magazine.account.dto.AccountDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountController {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    //TODO 혹시 스트링으로 넘기면 리다이렉트????
    //Home
    @GetMapping("/home")
    public String 홈(){
        System.out.println("홈");
        return "홈";
    }
    @GetMapping("/manager")
    public ResponseEntity<?> 매니저(){
        return ResponseEntity.ok().body("매니저");
    }


    //TODO 로그인 했을 때 응답보내기
    //TODO 매니저에서 토큰 없을 때 403 에러 보내기
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> Accountsignup(@Valid @RequestBody AccountDto accountDto, Errors errors){
        if(errors.hasErrors()){
            System.out.println("전체 에러 출력 :" +errors.getAllErrors() );
            for(Object sd: errors.getAllErrors() ){
                System.out.println(sd);
            }
            System.out.println(errors.getErrorCount());
            System.out.println(errors.getFieldError().getField());
            System.out.println(errors.getObjectName());
            System.out.println(errors.getFieldError().getDefaultMessage());

            return ResponseEntity.badRequest().body("입력 형식이 잘못되었습니다.");
        }
        System.out.println(accountDto);

        AccountRoleEnum role = AccountRoleEnum.USER;
        accountDto.setRole(role);

        Account account = accountDto.toEntity();
        account.encodePassword(passwordEncoder);
        System.out.println(account);
        accountRepository.save(account);

        return ResponseEntity.ok().body(account);
    }

}
