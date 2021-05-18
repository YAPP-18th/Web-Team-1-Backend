package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
// 프로젝트에 AppProperties를 사용할 수 있도록 선언해주면 에러 사라짐
// 프로퍼티를 읽어오는 방법 중 가장 많이 쓰임
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Auth {
        private String tokenSecret;
        private long accessTokenExpirationMsec;
        private long refreshTokenExpirationMsec;

        public Auth(String tokenSecret, long accessTokenExpirationMsec, long refreshTokenExpirationMsec) {
            this.tokenSecret = tokenSecret;
            this.accessTokenExpirationMsec = accessTokenExpirationMsec;
            this.refreshTokenExpirationMsec = refreshTokenExpirationMsec;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class OAuth2 {
        private List<String> authorrizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorrizedRedirectUris = authorrizedRedirectUris;
            return this;
        }
    }
}
