package com.yapp18.retrospect.service;

import com.yapp18.retrospect.auth.dto.OAuthAttributes;
import com.yapp18.retrospect.auth.dto.SessionUser;
import com.yapp18.retrospect.auth.helper.SocialPlatform;
import com.yapp18.retrospect.auth.social.GoogleOauth;
import com.yapp18.retrospect.auth.social.KakaoOauth;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

@Service
@RequiredArgsConstructor
/*스프링 시큐리티에서 지원하는 OAuth2UserService 인터페이스를 구현
이 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원합니다.

스프링 부트 2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL을 지원하고 있습니다.
사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들 필요가 없습니다. 시큐리티에서 이미 구현해 놓은 상태입니다.*/
public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //userRequest = {도메인}/login/oauth2/code/{소셜서비스코드}로 날아오는 요청?
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //registrationId - 현재 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //userNameAttributeName - OAuth2 로그인 진행 시 키가 되는 필드 값을 이야기 합니다. Primary Key와 같은 의미입니다.
        /*표준 OAuth 2.0 Provider의 경우 UserInfo 응답에서 사용자 이름에 액세스하는 데 사용되는 속성 이름이 필요하므로
        UserInfoEndpoint.getUserNameAttributeName()을 통해 사용할 수 있어야합니다.*/
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        //OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스입니다.
        //이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용합니다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //oAuth2User.getAttributes() 얘가 핵심?

        //여기서 저장이 안되어 있다면 가입 페이지로 redirect 해야함
        User user = saveOrUpdate(attributes);

        //SessionUser - 세션에 사용자 정보를 저장하기 위한 Dto 클래스입니다.
        if(registrationId.equals("google")) httpSession.setAttribute("google", new SessionUser(user));
        else httpSession.setAttribute("kakao", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture())) // 결괏값(entity)가 존재한다면, 즉 DB에 존재한다면 update
                .orElse(attributes.toEntity()); //없다면 새로운 엔티티 생성

        return userRepository.save(user);
    }

//    private final GoogleOauth googleOauth;
//    private final KakaoOauth kakaoOauth;
//    private final HttpServletResponse response;
//
//    public void request(SocialPlatform socialPlatform){
//        String redirectURL;
//        switch (socialPlatform) {
//            case GOOGLE: {
//                redirectURL = googleOauth.getOauthRedirectURL();
//                break;
//            }
//            case KAKAO: {
//                redirectURL = kakaoOauth.getOauthRedirectURL();
//                break;
//            }
//            default: {
//                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
//            }
//        }
//        try {
//            response.sendRedirect(redirectURL);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
