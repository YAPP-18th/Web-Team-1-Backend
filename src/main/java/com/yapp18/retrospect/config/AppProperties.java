package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
// 프로젝트에 AppProperties를 사용할 수 있도록 선언해주면 에러 사라짐
// 프로퍼티를 읽어오는 방법 중 가장 많이 쓰임
@ConfigurationProperties(prefix = "app")  // app: 하위에 있는 값들을 자동으로 읽어옴
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Auth {
        private String accessTokenSecret;
        private String refreshTokenSecret;
        private long accessTokenExpirationMsec;
        private long refreshTokenExpirationMsec;


        public Auth(String accessTokenSecret, String refreshTokenSecret, long accessTokenExpirationMsec, long refreshTokenExpirationMsec, String redirectUri) {
            this.accessTokenSecret = accessTokenSecret;
            this.refreshTokenSecret = refreshTokenSecret;
            this.accessTokenExpirationMsec = accessTokenExpirationMsec;
            this.refreshTokenExpirationMsec = refreshTokenExpirationMsec;
        }
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class OAuth2 {
        private String redirectUri;
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2(String redirectUri, List<String> authorizedRedirectUris) {
            this.redirectUri = redirectUri;
            this.authorizedRedirectUris = authorizedRedirectUris;
        }
    }
}
