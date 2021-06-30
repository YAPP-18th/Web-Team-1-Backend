package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.security.UserPrincipal;
import com.yapp18.retrospect.web.advice.TokenException;
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

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
            if (validateAccessToken(request, bearerToken, appProperties.getAuth().getAccessTokenSecret())) {
                return bearerToken;
            }
        } else {
            request.setAttribute("errorCode", TokenErrorInfo.ILLEGAL_GRANTTYPE.getCode());
        }
        return null;
    }

    public String getExpiredTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)){
            if(bearerToken.startsWith("Bearer ")) {
                bearerToken = bearerToken.substring(7);
                try {
                    Jwts.parser().setSigningKey(appProperties.getAuth().getAccessTokenSecret()).parseClaimsJws(bearerToken);
                } catch (ExpiredJwtException e) {
                    return bearerToken;
                } catch (SignatureException e) {
                    throw new TokenException(TokenErrorInfo.INVALID_SIGNATURE_ACCESS);
                } catch (MalformedJwtException e) {
                    throw new TokenException(TokenErrorInfo.MALFORMED_ACCESS);
                }  catch (UnsupportedJwtException e) {
                    throw new TokenException(TokenErrorInfo.UNSUPPORTED_ACCESS);
                }
            } else {
                throw new TokenException(TokenErrorInfo.ILLEGAL_GRANTTYPE);
            }
        } else {
            throw new TokenException(TokenErrorInfo.ILLEGAL_ARGUMENT_ACCESS);
        }
        return null;
//        throw new TokenException(TokenErrorInfo.NOT_EXPIRED_ACCESS);
    }

    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claim = new HashMap<>();
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claim.put("user_idx", userPrincipal.getUser().getUserIdx());
        claim.put("email", userPrincipal.getUser().getEmail());
        claim.put("nickname", userPrincipal.getUser().getNickname());
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

//    public Optional<AuthDto.ReissueResponse> reissueAccessToken(HttpServletRequest request) {
//
//        Optional<String> refreshOptional = CookieUtils.getCookie(request, "JWT-Refresh-Token")
//                 .map(Cookie::getValue);
//        if(!refreshOptional.isPresent()) {
//            throw new RuntimeException("쿠키에 Refresh Token이 존재하지 않습니다.");
//        }
//        String refreshToken = refreshOptional.get();
//        String expiredAccessToken = getTokenFromRequest(request);
//        String accessSecret = appProperties.getAuth().getAccessTokenSecret();
//        String refreshSecret = appProperties.getAuth().getRefreshTokenSecret();
//        if(validateExpiredToken(request, expiredAccessToken, accessSecret) &&
//                validateToken(request, refreshToken, refreshSecret)) {
//            Claims accessClaims = getClaimsFromToken(expiredAccessToken, accessSecret);
//            Claims refreshClaims = getClaimsFromToken(refreshToken, refreshSecret);
//            Number accessIdx = (Number) accessClaims.get("user_idx");
//            Number refreshIdx = (Number) refreshClaims.get("user_idx");
//            if(!accessIdx.equals(refreshIdx)){
//                throw new RuntimeException("Access Token과 Refresh Token의 내용이 일치하지 않습니다.");
//            }
//
//            Authentication authentication = getAuthentication(expiredAccessToken, accessSecret);
//            String reissuedAccessToken = createAccessToken(authentication);
//            AuthDto.ReissueResponse response = AuthDto.ReissueResponse
//                    .builder()
//                    .grantType(BEARER_TYPE)
//                    .accessToken(reissuedAccessToken)
//                    .build();
//            return Optional.of(response);
//        }
//        return Optional.empty();
//    }
  
    public AuthDto.ReissueResponse reissueAccessToken(HttpServletRequest request, AuthDto.ReissueRequest reissueRequest) {
        String expiredAccessToken = getExpiredTokenFromRequest(request);
        String refreshToken = reissueRequest.getRefreshToken();
        String accessSecret = appProperties.getAuth().getAccessTokenSecret();
        String refreshSecret = appProperties.getAuth().getRefreshTokenSecret();
        validateRefreshToken(refreshToken, refreshSecret);
        if (expiredAccessToken != null) {
            Number accessIdx = (Number) getUserIdxFromExpiredToken(expiredAccessToken, accessSecret);
            Claims refreshClaims = getClaimsFromToken(refreshToken, refreshSecret);
            Number refreshIdx = (Number) refreshClaims.get("user_idx");
            if (!accessIdx.equals(refreshIdx)) {
                throw new TokenException(TokenErrorInfo.UNMATCHED_TOKEN_PAYLOAD);
            }

            Authentication authentication = getAuthentication(refreshToken, refreshSecret);
            String reissuedAccessToken = createAccessToken(authentication);
            AuthDto.ReissueResponse response = AuthDto.ReissueResponse
                    .builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(reissuedAccessToken)
                    .build();
            return response;
        }
        return null;
    }

    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claim = new HashMap<>();
        claim.put("user_idx", userPrincipal.getUser().getUserIdx());
        claim.put("email", userPrincipal.getUser().getEmail());
        claim.put("nickname", userPrincipal.getUser().getNickname());

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
        String email = (String) claims.get("email");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);// OK

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateAccessToken(HttpServletRequest request, String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }  catch (ExpiredJwtException e) {
            System.out.println(e.getMessage());
            logger.error(TokenErrorInfo.EXPIRED_ACCESS.getMessage());
            request.setAttribute("errorCode", TokenErrorInfo.EXPIRED_ACCESS.getCode());
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            logger.error(TokenErrorInfo.INVALID_SIGNATURE_ACCESS.getMessage());
            request.setAttribute("errorCode", TokenErrorInfo.INVALID_SIGNATURE_ACCESS.getCode());
        } catch (MalformedJwtException e) {
            System.out.println(e.getMessage());
            logger.error(TokenErrorInfo.MALFORMED_ACCESS.getCode());
            request.setAttribute("errorCode", TokenErrorInfo.MALFORMED_ACCESS.getCode());
        }  catch (UnsupportedJwtException e) {
            System.out.println(e.getMessage());
            logger.error(TokenErrorInfo.UNSUPPORTED_ACCESS.getMessage());
            request.setAttribute("errorCode", TokenErrorInfo.UNSUPPORTED_ACCESS.getCode());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            logger.error(TokenErrorInfo.ILLEGAL_ARGUMENT_ACCESS.getMessage());
            request.setAttribute("errorCode", TokenErrorInfo.ILLEGAL_ARGUMENT_ACCESS.getCode());
        }
        return false;
    }

//    public boolean validateExpiredToken(HttpServletRequest request, String token, String secret) {
//        try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//        } catch (ExpiredJwtException e) {
//            return true;
//        } catch (SignatureException e) {
//            System.out.println(e.getMessage());
//            logger.error(TokenErrorInfo.INVALID_SIGNATURE.getMessage());
//            request.setAttribute("errorCode", TokenErrorInfo.INVALID_SIGNATURE.getCode());
//        } catch (MalformedJwtException e) {
//            System.out.println(e.getMessage());
//            logger.error(TokenErrorInfo.MALFORMED_JWT.getCode());
//            request.setAttribute("errorCode", TokenErrorInfo.MALFORMED_JWT.getCode());
//        }  catch (UnsupportedJwtException e) {
//            System.out.println(e.getMessage());
//            logger.error(TokenErrorInfo.UNSUPPORTED_JWT.getMessage());
//            request.setAttribute("errorCode", TokenErrorInfo.UNSUPPORTED_JWT.getCode());
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//            logger.error(TokenErrorInfo.ILLEGAL_ACCESS_ARGUMENT.getMessage());
//            request.setAttribute("errorCode", TokenErrorInfo.ILLEGAL_ACCESS_ARGUMENT.getCode());
//        }
//        return false;
//    }

    public void validateRefreshToken(String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        }  catch (ExpiredJwtException e) {
            throw new TokenException(TokenErrorInfo.EXPIRED_REFRESH);
        } catch (SignatureException e) {
            throw new TokenException(TokenErrorInfo.INVALID_SIGNATURE_REFRESH);
        } catch (MalformedJwtException e) {
            throw new TokenException(TokenErrorInfo.MALFORMED_REFRESH);
        }  catch (UnsupportedJwtException e) {
            throw new TokenException(TokenErrorInfo.UNSUPPORTED_REFRESH);
        } catch (IllegalArgumentException e) {
            throw new TokenException(TokenErrorInfo.ILLEGAL_ARGUMENT_REFRESH);
        }
    }

    public Claims getClaimsFromToken(String token, String secret){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Object getUserIdxFromExpiredToken(String token, String secret){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims().get("user_idx");
        }
        return null;
    }

    public Long getUserIdx(String accessToken){
        return getClaimsFromToken(accessToken, appProperties.getAuth().getAccessTokenSecret())
                .get("user_idx", Long.class);
    }
}
