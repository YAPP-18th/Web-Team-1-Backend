package com.yapp18.retrospect.security.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    Map<String, Object> kakaoAccount;
    Map<String, Object> profile;
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) kakaoAccount.get("profile");
    }
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getPicture() {
        return (String) profile.get("profile_image_url");
    }
}
