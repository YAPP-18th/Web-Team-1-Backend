package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.auth.dto.LoginUser;
import com.yapp18.retrospect.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    //페이지에 관련된 컨트롤러는 모두 IndexController를 사용합니다.

    //머스테치의 파일 위치는 기본적으로 src/main/resources/templates입니다
    //이 위치에 머스테치 파일을 두면 스프링 부트에서 자동으로 로딩합니다.
//    private final HttpSession httpSession;
//
//    @GetMapping("/")
//    public String index(Model model, @LoginUser SessionUser user){
//        //Model = 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있습니다.
//
//        //앞서 작성된 CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장하도록 구성했습니다.
//        //before)즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있습니다.
//        //after)기존에 (User) httpSession.getAttribute("user)로 가져오던 세션 정보 값이 개선 되었습니다.
//        //이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 되었습니다.
//        if(user != null) {
//            model.addAttribute("googleName", user.getName());
//        }
//        return "index";
//    }
}