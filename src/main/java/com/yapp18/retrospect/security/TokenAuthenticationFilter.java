package com.yapp18.retrospect.security;

import com.yapp18.retrospect.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//이 클래스는 요청으로부터 전달된 JWT 토큰 검증하는데 사용된다.
//전달된 Request에서 JWT 토큰을 가져오고, 가져온 토큰의 유효성 검사 후, 토큰에 있는 사용자 Id를 가져온다.
//가져온 사용자 Id로 사용자 정보를 가져오고, 이 정보로 UsernamePasswordAuthenticationToken을 만들어 인증하는 과정을 거친다.
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    // API 호출은 전부 JWT를 확인한다.
//    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**");

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request Header 에서 Access Token 을 꺼내고 여러가지 검사 후 유저 정보를 꺼내서 SecurityContext 에 저장합니다.
        //가입/로그인/재발급을 제외한 모든 Request 요청은 이 필터를 거치기 때문에 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행되지 않습니다.
        //그리고 요청이 정상적으로 Controller 까지 도착했다면 SecurityContext 에 Member ID 가 존재한다는 것이 보장됩니다.
        try {
            String jwt = tokenService.getTokenFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                //매 토큰 인증마다 DB를 통해 유저 정보를 불러오는 것은 Stateless 하지 않음
                //SecurityContextHolder.getContext에 Authentication값을 세팅시 유저의 모든 정보를 세팅할 필요는 없고 권한 정보만 세팅해도 됩니다.
                UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(jwt);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            logger.error("Security Context에서 사용자 인증을 설정할 수 없습니다", exception);
        }
        filterChain.doFilter(request, response);
    }
}
