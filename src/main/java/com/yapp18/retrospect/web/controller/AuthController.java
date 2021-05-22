package com.yapp18.retrospect.web.controller;

//AWS 책에선 Spring Boot 프로젝트에 뷰가 같이 있었으므로 컨트롤러 없이 내부적으로 처리?
//<a href="/oauth2/authorization/google"> : 스프링 시큐리티에서 제공하는 로그인 url

import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final TokenService tokenService;

    @GetMapping("/oauth/header")
    public Map<String, Object> getHeader(@RequestHeader Map<String, Object> requestHeader){
        return requestHeader;
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = tokenService.createRefreshToken(authentication);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
}
