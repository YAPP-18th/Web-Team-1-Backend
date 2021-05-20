package com.yapp18.retrospect.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
//Spring OAuth2는 기본적으로 HttpSessionOAuth2AuthorizationRequestRepository를 사용해 Authorization Request를 저장한다.
// 하지만 JWT의 경우 Session에 저장할 필요가 없으므로 HttpCookieOAuth2AuthorizationRequestRepository 클래스를 생성하여
// Authorization Request를 Based64 encoded cookie에 저장한다.
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        //1. request에서 oauth2_auth_request라는 이름을 가진 쿠키를 가져온다
        //2. 가져온 쿠키를 역직렬화한 후 OAuth2AuthorizationRequest.class 형태로 캐스팅한다
        //3. 쿠키가 존제하지 않다면 null을 반환
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) { // oauth2_auth_request, redirect_uri 쿠키 값을 삭제한다
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        //1. addCookie로 response에 "oauth2_auth_request" 이름으로 authorizationRequest를 직렬화 한 값을 담는다.
        //2. request에서 redirect_uri을 추출하여 redirectUriAfterLogin에 담는다.
        //3. 만약 redirectUriAfterLogin이 존재한다면 response에 "redirect_uri" 이름으로 추출한 redirectUriAfterLogin 값을 담는다.
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}