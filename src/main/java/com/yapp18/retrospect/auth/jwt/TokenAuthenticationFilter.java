package com.yapp18.retrospect.auth.jwt;

import com.yapp18.retrospect.auth.dto.OAuthAttributes;
import com.yapp18.retrospect.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//이 클래스는 요청으로부터 전달된 JWT 토큰 검증하는데 사용된다.
//전달된 Request에서 JWT 토큰을 가져오고, 가져온 토큰의 유효성 검사 후, 토큰에 있는 사용자 Id를 가져온다.
//가져온 사용자 Id로 사용자 정보를 가져오고, 이 정보로 UsernamePasswordAuthenticationToken을 만들어 인증하는 과정을 거친다.
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        return bearerToken;
    }
}
