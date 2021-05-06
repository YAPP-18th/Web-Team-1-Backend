package com.yapp18.retrospect.web.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {
    @RequestMapping("/")
    private static String hello(){
        return "hello";
    }

    @RequestMapping("/signup")
    private static String signup(){
        return "signup";
    }

    @RequestMapping("/ind")
    private static String index(){
        return "index";
    }
}
