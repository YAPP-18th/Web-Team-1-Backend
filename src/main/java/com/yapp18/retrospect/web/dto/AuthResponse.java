package com.yapp18.retrospect.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResponse {

    private String refreshToken;
    private String tokenType = "Bearer"; // 인증 방식

    @Builder
    public AuthResponse(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}