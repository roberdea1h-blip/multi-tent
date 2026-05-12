package com.multi.tent.pos_api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private static final String COOKIE_NAME = "access_token";

    @Value("${jwt.expiration}")
    private long accessExpiration;

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(accessExpiration / 1000)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie removeAccessTokenCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
