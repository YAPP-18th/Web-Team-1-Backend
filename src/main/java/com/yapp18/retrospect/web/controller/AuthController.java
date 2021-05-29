package com.yapp18.retrospect.web.controller;

//AWS 책에선 Spring Boot 프로젝트에 뷰가 같이 있었으므로 컨트롤러 없이 내부적으로 처리?
//<a href="/oauth2/authorization/google"> : 스프링 시큐리티에서 제공하는 로그인 url

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.AuthDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(value = "AuthController")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenService tokenService;

    @ApiOperation(value = "auth", notes = "[인증] Access Token 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<Object> reissue (HttpServletRequest request) {
        return new ResponseEntity<>(ApiDefaultResponse.res(200,
                ResponseMessage.AUTH_REISSUE.getResponseMessage(),
                tokenService.reissueAccessToken(request)),
                HttpStatus.OK);
    }

//    @PostMapping("/reissue")
//    public ResponseEntity<AuthDto.ReissueResponse> reissue (@ApiParam(value = "만료된 Access Token과 유효한 Refresh Token", required = true)
//                                                            @RequestBody AuthDto.ReissueRequest request) {
//        return ResponseEntity.ok(tokenService.reissueAccessToken(request));
//    }
}
