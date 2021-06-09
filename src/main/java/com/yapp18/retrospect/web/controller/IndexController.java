package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class IndexController {
    //리버스 프록시, 로드 밸런싱 확인용 애플리케이션 고유 아이디
    UUID uuid = UUID.randomUUID();

    @GetMapping("/")
    public String index(){
        return uuid.toString();
    }
}