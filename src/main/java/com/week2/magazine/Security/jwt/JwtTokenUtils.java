package com.week2.magazine.Security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.week2.magazine.Security.UserDetailsImpl;
import com.week2.magazine.account.AccountRoleEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;

public final class JwtTokenUtils {

    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    // JWT 토큰의 유효기간: 3일 (단위: seconds)
    private static final int JWT_TOKEN_VALID_SEC = 3 * DAY;
    // JWT 토큰의 유효기간: 3일 (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000;

    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";

    public static final String CLAIM_USER_ID = "USER_ID";
    public static final String CLAIM_USER_NAME = "USER_NAME";
    public static final String CLAIM_USER_ROLE = "USER_ROLE";

    public static final String JWT_SECRET = "jwt_secret_!@#$%";

    public static String generateJwtToken(UserDetailsImpl userDetails) {
        System.out.println("로그인 필터 8");
        String token = null;
        System.out.println(userDetails.getId());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getRole());

        try {
            token = JWT.create()
                    .withIssuer("sparta")
                    .withClaim(CLAIM_USER_ID, userDetails.getId())
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_USER_ROLE, String.valueOf(userDetails.getRole()))
                     // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }
}
