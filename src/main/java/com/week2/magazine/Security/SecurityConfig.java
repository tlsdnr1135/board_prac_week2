package com.week2.magazine.Security;

import com.week2.magazine.Security.filter.FormLoginFilter;
import com.week2.magazine.Security.filter.JwtAuthFilter;
import com.week2.magazine.Security.jwt.HeaderTokenExtractor;
import com.week2.magazine.Security.provider.FormLoginAuthProvider;
import com.week2.magazine.Security.provider.JWTAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    private final CorsFilter corsFilter;


    public SecurityConfig(
            JWTAuthProvider jwtAuthProvider,
            HeaderTokenExtractor headerTokenExtractor,
            CorsFilter corsFilter
    ) {
        this.corsFilter=corsFilter;
        this.jwtAuthProvider = jwtAuthProvider;
        this.headerTokenExtractor = headerTokenExtractor;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) {
// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilter(corsFilter);
        http.csrf().disable();

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
//        http.addFilter(corsFilter);
//        http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
//        http.addFilter(new JwtAuthorizationFilter(authenticationManager(),accountRepository));
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);


        //TODO mvcMatchers 하고 authorizatino 차이
        http.authorizeHttpRequests()
//                .mvcMatchers(HttpMethod.GET,"/h2-console/**").permitAll()
                .antMatchers("/api/manager").hasRole("USER")
                .anyRequest().permitAll();
        }
//        http.authorizeRequests()
//                .anyRequest()
//                .permitAll()
//                .and()
//    // [로그아웃 기능]
//                .logout()
//    // 로그아웃 요청 처리 URL
//                .logoutUrl("/user/logout")
//                .permitAll()
//                .and()
//                .exceptionHandling()
//    // "접근 불가" 페이지 URL 설정
//                .accessDeniedPage("/forbidden.html");
        @Bean
        public FormLoginFilter formLoginFilter() throws Exception {
            FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
            formLoginFilter.setFilterProcessesUrl("/api/login");
            formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
            formLoginFilter.afterPropertiesSet(); //TODO 찾아보기 -> formLoginFilter.afterPropertiesSet
            return formLoginFilter;
        }
        @Bean
        public FormLoginSuccessHandler formLoginSuccessHandler() {
            return new FormLoginSuccessHandler();
        }
        @Bean
        public FormLoginAuthProvider formLoginAuthProvider() {
            return new FormLoginAuthProvider(passwordEncoder());//TODO 이걸 왜 넣지?
        }


        private JwtAuthFilter jwtFilter() throws Exception {
            List<String> skipPathList = new ArrayList<>();

            // Static 정보 접근 허용
            skipPathList.add("GET,/images/**");
            skipPathList.add("GET,/css/**");

            // h2-console 허용
            skipPathList.add("GET,/h2-console/**");
            skipPathList.add("POST,/h2-console/**");

            // 회원 관리 API 허용
            skipPathList.add("GET,/");
            skipPathList.add("POST,/api/signup");
            skipPathList.add("GET,/api/home");

            //보드게시판 API 허용
            skipPathList.add("GET,/api/boards");

//            skipPathList.add("GET,/");
//            skipPathList.add("GET,/basic.js");
//
//            skipPathList.add("GET,/favicon.ico");

            FilterSkipMatcher matcher = new FilterSkipMatcher(
                    skipPathList,
                    "/**"
            );

            JwtAuthFilter filter = new JwtAuthFilter(
                    matcher,
                    headerTokenExtractor
            );
            filter.setAuthenticationManager(super.authenticationManagerBean());

            return filter;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }




}
