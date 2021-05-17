package com.yapp18.retrospect.service;

import com.yapp18.retrospect.auth.dto.OAuthAttributes;
import com.yapp18.retrospect.auth.helper.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Date;

// 이 클래스는 유효한 JWT를 생성해준다. (JWT Properties 정보를 담고 있는 클래스 사용)
@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private AppProperties appProperties;

    public TokenService(AppProperties appProperties){
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication){
        OAuthAttributes oAuthAttributes = (OAuthAttributes) authentication.getPrincipal();
        System.out.println("createToken : " + oAuthAttributes);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMSec());

        return Jwts.builder()
                .setSubject(oAuthAttributes.getEmail())
                .setSubject(oAuthAttributes.getPlatform())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

    }

    public String getUserEmailFromToken(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(appProperties.getAuth().getTokenSecret())
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("유효하지 않은 JWT 서명");
        } catch (MalformedJwtException ex) {
            logger.error("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException ex) {
            logger.error("만료된 JWT 토큰");
        } catch (UnsupportedJwtException ex) {
            logger.error("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException ex) {
            logger.error("비어있는 JWT");
        }
        return false;
    }
}
