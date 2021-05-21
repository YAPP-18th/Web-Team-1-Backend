package com.yapp18.retrospect.security;

import com.yapp18.retrospect.service.CustomUserDetailsService;
import com.yapp18.retrospect.service.TokenService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

//이 클래스는 요청으로부터 전달된 JWT 토큰 검증하는데 사용된다.
//전달된 Request에서 JWT 토큰을 가져오고, 가져온 토큰의 유효성 검사 후, 토큰에 있는 사용자 Id를 가져온다.
//가져온 사용자 Id로 사용자 정보를 가져오고, 이 정보로 UsernamePasswordAuthenticationToken을 만들어 인증하는 과정을 거친다.
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // API 호출은 전부 JWT를 확인한다.
//    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**");

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                Claims claims = tokenService.getClaimsFromToken(jwt);
                Number idx = (Number) claims.get("user_idx");
                Long userIdx = idx.longValue();
                String nickname = (String) claims.get("nickname");

                UserDetails userDetails = customUserDetailsService.loadUserByUserIdx(userIdx);// OK

                if (logger.isDebugEnabled()) {
                    logger.debug("JWT" + jwt);
                    logger.debug("userIdx::" + userIdx);
                    logger.debug("nickname::" + nickname);
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            logger.error("Security Context에서 사용자 인증을 설정할 수 없습니다", exception);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
