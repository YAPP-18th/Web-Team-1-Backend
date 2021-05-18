package com.yapp18.retrospect.auth.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// Email과 Platform 조합으로 토큰 객체를 생성해준다.
@Getter
public class EmailPlatformAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;

    //mail과 Platform을 받아 만들어진 인증 전 객체이기 때문에 isAuthenticated는 false가 된다.
    public EmailPlatformAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }
    //mail과 Platform, authorities를 받아 생성된 토큰은 인증이 완료된 인증 후 객체이다.
    public EmailPlatformAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }
}
