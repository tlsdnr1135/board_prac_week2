package com.week2.magazine.Security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtPreProcessingToken extends UsernamePasswordAuthenticationToken {

    private JwtPreProcessingToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtPreProcessingToken(String token) {
        this(token, token.length());
    }
}
