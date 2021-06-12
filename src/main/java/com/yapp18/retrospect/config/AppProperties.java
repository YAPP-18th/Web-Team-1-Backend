package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
// 프로젝트에 AppProperties를 사용할 수 있도록 선언해주면 에러 사라짐
// 프로퍼티를 읽어오는 방법 중 가장 많이 쓰임
@ConfigurationProperties(prefix = "app")  // app: 하위에 있는 값들을 자동으로 읽어옴
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    private final Values values = new Values();

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Auth {
        private String accessTokenSecret;
        private String refreshTokenSecret;
        private long accessTokenExpirationMsec;
        private long refreshTokenExpirationMsec;

        public Auth(String accessTokenSecret, String refreshTokenSecret, long accessTokenExpirationMsec, long refreshTokenExpirationMsec) {
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

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Values {
        private String defaultProfileUrl;
        private String defaultNicknameSuffix;
        private Map<String, List<String>> allUrls;
        private Map<String, List<String>> memberUrls;
        private Map<String, List<String>> adminUrls;
        private Map<String, List<String>> anonymousUrls;

        public Values(String defaultProfileUrl, String defaultNicknameSuffix, Map<String, List<String>> allUrls, Map<String, List<String>> memberUrls, Map<String, List<String>> adminUrls, Map<String, List<String>> anonymousUrls) {
            this.defaultProfileUrl = defaultProfileUrl;
            this.defaultNicknameSuffix = defaultNicknameSuffix;
            this.allUrls = allUrls;
            this.memberUrls = memberUrls;
            this.adminUrls = adminUrls;
            this.anonymousUrls = anonymousUrls;
        }
    }
}
