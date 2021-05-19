package com.yapp18.retrospect.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;

    @Builder
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
