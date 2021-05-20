package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 이 클래스는 유효한 JWT를 생성해준다. (JWT Properties 정보를 담고 있는 클래스 사용)
@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private AppProperties appProperties;

    public TokenService(AppProperties appProperties){
        this.appProperties = appProperties;
    }

    public <T> String createAccessToken(T userDetails){
        Map<String,Object> claim = new HashMap<>();

        if (userDetails instanceof DefaultOAuth2User) { // kakao도 맞게 설정할 것
            //nameattributekey로 구분하는 방법? -> DefaultOAuth2User를 상속하는 userPrinciple 클래스 만들기?
            Map<String,Object> attributes = ((DefaultOAuth2User) userDetails).getAttributes();
            if(attributes.get("sub") != null){ // google
                claim.put("providerId",  ((DefaultOAuth2User) userDetails).getName()); // subject 인증 대상(고유 ID)
                claim.put("email", attributes.get("email"));
            } else { // kakao
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                claim.put("providerId",  ((DefaultOAuth2User) userDetails).getName()); // subject 인증 대상(고유 ID)
                claim.put("email", kakaoAccount.get("email"));
            }
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getAccessTokenSecret().getBytes())
                .compact();
    }

    public <T> String createRefreshToken(T userDetails){
        Map<String,Object> claim = new HashMap<>();

        if (userDetails instanceof DefaultOAuth2User) { // kakao도 맞게 설정할 것
            //nameattributekey로 구분하는 방법? -> DefaultOAuth2User를 상속하는 userPrinciple 클래스 만들기?
            Map<String,Object> attributes = ((DefaultOAuth2User) userDetails).getAttributes();
            if(attributes.get("sub") != null){ // google
                claim.put("providerId",  ((DefaultOAuth2User) userDetails).getName()); // subject 인증 대상(고유 ID)
                claim.put("email", attributes.get("email"));
            } else { // kakao
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                claim.put("providerId",  ((DefaultOAuth2User) userDetails).getName()); // subject 인증 대상(고유 ID)
                claim.put("email", kakaoAccount.get("email"));
            }
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getRefreshTokenSecret().getBytes())
                .compact();
    }

    public String getUserEmailFromToken(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(appProperties.getAuth().getAccessTokenSecret())
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getAccessTokenSecret()).parseClaimsJws(accessToken);
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

    public Map<String,Object> getBobyFromToken(String accessToken){
        return Jwts.parser().setSigningKey(appProperties.getAuth().getAccessTokenSecret()).parseClaimsJws(accessToken).getBody();
    }
}
