package com.yapp18.retrospect.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@AllArgsConstructor
public class ProfileController {
    private Environment env;

    @GetMapping("/profile")
    public String getProfile() {
        //프로젝트의 환경설정 값을 다루는 Environment Bean을 DI받아 현재 활성화된 Profile을 반환
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
