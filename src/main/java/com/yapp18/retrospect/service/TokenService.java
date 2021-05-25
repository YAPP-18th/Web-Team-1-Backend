package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.security.UserPrincipal;
import com.yapp18.retrospect.web.dto.AuthDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 이 클래스는 유효한 JWT를 생성해준다. (JWT Properties 정보를 담고 있는 클래스 사용)
@Service
@RequiredArgsConstructor
public class TokenService {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final AppProperties appProperties;
    private final CustomUserDetailsService customUserDetailsService;

    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claim = new HashMap<>();
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claim.put("user_idx", userPrincipal.getUserIdx());
        claim.put("nickname", userPrincipal.getNickname());
        claim.put(AUTHORITIES_KEY, authorities);



        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getAccessTokenSecret())
                .compact();
    }

    public AuthDto.ReissueResponse reissueAccessToken(AuthDto.ReissueRequest reissueRequest) {
        if(validateToken(reissueRequest.getRefreshToken())){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다. 로그아웃합니다.");
        }
        String expiredAccessToken = reissueRequest.getAccessToken();
        String refreshToken = reissueRequest.getRefreshToken();

        Claims accessClaims = getClaimsFromToken(expiredAccessToken);
        Claims refreshClaims = getClaimsFromToken(refreshToken);
        Number accessIdx = (Number) accessClaims.get("user_idx");
        Number refreshIdx = (Number) refreshClaims.get("user_idx");
        if(!accessIdx.equals(refreshIdx)){
            throw new RuntimeException("Access Token과 Refresh Token의 내용이 일치하지 않습니다. 로그아웃합니다.");
        }

        Authentication authentication = getAuthentication(expiredAccessToken);
        String reissuedAccessToken = createAccessToken(authentication);

        return AuthDto.ReissueResponse
                .builder()
                .grantType(BEARER_TYPE)
                .accessToken(reissuedAccessToken)
                .build();
    }

    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claim = new HashMap<>();
        claim.put("user_idx", userPrincipal.getUserIdx());
        claim.put("nickname", userPrincipal.getNickname());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getRefreshTokenSecret())
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) { // 토큰 복호화
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getAccessTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        Number idx = (Number) claims.get("user_idx");
        Long userIdx = idx.longValue();
        String nickname = (String) claims.get("nickname");

        UserDetails userDetails = customUserDetailsService.loadUserByUserIdx(userIdx);// OK

//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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

    public Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(appProperties.getAuth().getAccessTokenSecret()).parseClaimsJws(token).getBody();
    }

    public Long getUserIdx(String accessToken){
        return getClaimsFromToken(accessToken)
                .get("user_idx", Long.class);
    }

}
