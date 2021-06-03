package com.yapp18.retrospect.web.controller;

//AWS 책에선 Spring Boot 프로젝트에 뷰가 같이 있었으므로 컨트롤러 없이 내부적으로 처리?
//<a href="/oauth2/authorization/google"> : 스프링 시큐리티에서 제공하는 로그인 url

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.security.oauth2.CookieUtils;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.AuthDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Api(value = "AuthController")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenService tokenService;

//    @ApiOperation(value = "auth", notes = "[인증] Access Token 발급")
//    @GetMapping("/issue")
//    public ResponseEntity<Object> issue (HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
//        HttpHeaders headers = new HttpHeaders();
//        Optional cookieOptional = CookieUtils.getCookie(request, "JWT-Refresh-Token");
//        if(cookieOptional.isPresent()){
//            Cookie cookie = (Cookie) cookieOptional.get();
//            CookieUtils.addCookie(response, "JWT-Refresh-Token", cookie.getValue(), true, 180);
//        }
//        headers.setLocation(new URI("http://localhost:3000"));
//        return new ResponseEntity<>(ApiDefaultResponse.res(200,
//                ResponseMessage.AUTH_ISSUE.getResponseMessage(),
//                tokenService.issueAccessToken(request)),
//                headers,
//                HttpStatus.OK);
//    }

    @ApiOperation(value = "auth", notes = "[인증] Access Token 재발급, 쿠키에 JWT-Refresh-Token 필요")
    @PostMapping("/reissue")
    public ResponseEntity<Object> reissue (HttpServletRequest request, HttpServletResponse response) {
        Optional cookieOptional = CookieUtils.getCookie(request, "JWT-Refresh-Token");
        if(cookieOptional.isPresent()){
            Cookie cookie = (Cookie) cookieOptional.get();
            CookieUtils.addCookie(response, "JWT-Refresh-Token", cookie.getValue(), true, 180);
        }
        return new ResponseEntity<>(ApiDefaultResponse.res(200,
                ResponseMessage.AUTH_REISSUE.getResponseMessage(),
                tokenService.reissueAccessToken(request)),
                HttpStatus.OK);
    }
}
