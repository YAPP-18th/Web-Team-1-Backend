package com.yapp18.retrospect.web.controller;

//AWS 책에선 Spring Boot 프로젝트에 뷰가 같이 있었으므로 컨트롤러 없이 내부적으로 처리?
//<a href="/oauth2/authorization/google"> : 스프링 시큐리티에서 제공하는 로그인 url

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class OAuthController {
    @GetMapping("/oauth/header")
    public Map<String, Object> getHeader(@RequestHeader Map<String, Object> requestHeader){
        return requestHeader;
    }
}
