package com.yapp18.retrospect.auth.social;

//소셜 로그인 타입별로 공통적으로 사용될 interface
public interface SocialOauth {
    /*
     * 각 Social Login 페이지로 Redirect 처리할 URL Build
     * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
     */
    String getOauthRedirectURL();
}
