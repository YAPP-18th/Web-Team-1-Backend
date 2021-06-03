package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.security.UserPrincipal;
import com.yapp18.retrospect.security.oauth2.CookieUtils;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    public String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claim = new HashMap<>();
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claim.put("user_idx", userPrincipal.getUserIdx());
        claim.put("nickname", userPrincipal.getNickname());
        claim.put("isnew", userPrincipal.getIsNew());
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

    public Optional<AuthDto.IssueResponse> issueAccessToken(HttpServletRequest request) {

        Optional<String> refreshOptional = CookieUtils.getCookie(request, "JWT-Refresh-Token")
                .map(Cookie::getValue);
        if(!refreshOptional.isPresent()) {
            throw new RuntimeException("쿠키에 Refresh Token이 존재하지 않습니다.");
        }
        String refreshToken = refreshOptional.get();
        String refreshSecret = appProperties.getAuth().getRefreshTokenSecret();
        if(validateToken(request, refreshToken, refreshSecret)) {
            Authentication authentication = getAuthentication(refreshToken, refreshSecret);
            String accessToken = createAccessToken(authentication);
            AuthDto.IssueResponse response = AuthDto.IssueResponse
                    .builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .build();
            return Optional.of(response);
        }
        return Optional.empty();
    }

    public Optional<AuthDto.ReissueResponse> reissueAccessToken(HttpServletRequest request) {

        Optional<String> refreshOptional = CookieUtils.getCookie(request, "JWT-Refresh-Token")
                 .map(Cookie::getValue);
        if(!refreshOptional.isPresent()) {
            throw new RuntimeException("쿠키에 Refresh Token이 존재하지 않습니다.");
        }
        String refreshToken = refreshOptional.get();
        String expiredAccessToken = getTokenFromRequest(request);
        String accessSecret = appProperties.getAuth().getAccessTokenSecret();
        String refreshSecret = appProperties.getAuth().getRefreshTokenSecret();
        if(validateToken(request, expiredAccessToken, accessSecret)) {
            Claims accessClaims = getClaimsFromToken(expiredAccessToken, accessSecret);
            Claims refreshClaims = getClaimsFromToken(refreshToken, refreshSecret);
            Number accessIdx = (Number) accessClaims.get("user_idx");
            Number refreshIdx = (Number) refreshClaims.get("user_idx");
            if(!accessIdx.equals(refreshIdx)){
                throw new RuntimeException("Access Token과 Refresh Token의 내용이 일치하지 않습니다.");
            }

            Authentication authentication = getAuthentication(expiredAccessToken, accessSecret);
            String reissuedAccessToken = createAccessToken(authentication);
            AuthDto.ReissueResponse response = AuthDto.ReissueResponse
                    .builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(reissuedAccessToken)
                    .build();
            return Optional.of(response);
        }
        return Optional.empty();
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

    public UsernamePasswordAuthenticationToken getAuthentication(String token, String secret) { // 토큰 복호화
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
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

    public boolean validateToken(HttpServletRequest request, String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }  catch (ExpiredJwtException e) {
            System.out.println(e.getMessage());
            logger.error(ErrorInfo.EXPIRED_JWT.getMessage());
            request.setAttribute("exception", ErrorInfo.EXPIRED_JWT.getCode());
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            logger.error(ErrorInfo.INVALID_SIGNATURE.getMessage());
            request.setAttribute("exception", ErrorInfo.INVALID_SIGNATURE.getCode());
        } catch (MalformedJwtException e) {
            System.out.println(e.getMessage());
            logger.error(ErrorInfo.MALFORMED_JWT.getCode());
            request.setAttribute("exception", ErrorInfo.MALFORMED_JWT.getCode());
        }  catch (UnsupportedJwtException e) {
            System.out.println(e.getMessage());
            logger.error(ErrorInfo.UNSUPPORTED_JWT.getMessage());
            request.setAttribute("exception", ErrorInfo.UNSUPPORTED_JWT.getCode());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            logger.error(ErrorInfo.ILLEGAL_ARGUMENT.getMessage());
            request.setAttribute("exception", ErrorInfo.ILLEGAL_ARGUMENT.getCode());
        }
        return false;
    }

    public Claims getClaimsFromToken(String token, String secret){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Long getUserIdx(String accessToken){
        return getClaimsFromToken(accessToken, appProperties.getAuth().getAccessTokenSecret())
                .get("user_idx", Long.class);
    }
}
