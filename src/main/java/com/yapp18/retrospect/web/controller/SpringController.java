package com.yapp18.retrospect.web.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@Api(value = "SpringController") // swagger 리소스 명시
@RequestMapping("/api/v1/spring")
@AllArgsConstructor
public class SpringController {
    private Environment env;
    @GetMapping("/profiles")
    public String getProfiles() {
        //프로젝트의 환경설정 값을 다루는 Environment Bean을 DI받아 현재 활성화된 Profile을 반환
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
