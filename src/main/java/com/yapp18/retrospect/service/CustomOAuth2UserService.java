package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.exception.OAuth2AuthenticationProcessingException;
import com.yapp18.retrospect.security.UserPrincipal;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.security.oauth2.user.OAuth2UserInfo;
import com.yapp18.retrospect.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());;

        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("OAuth2 공급자(구글, 카카오, ...) 에서 이메일을 찾을 수 없습니다.");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(registrationId).toString())) {
                throw new OAuth2AuthenticationProcessingException(
                        user.getProvider() + " 계정을 사용하기 위해서 로그인을 해야합니다.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(registrationId, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }

    // DB에 존재하지 않을 경우 새로 등록
    private User registerNewUser(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(User.builder()
                .role(Role.MEMBER)
                .name(oAuth2UserInfo.getName())
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profile(oAuth2UserInfo.getProfile())
                .provider(registrationId)
                .providerId(oAuth2UserInfo.getId())
                .build()
        );
    }

    // DB에 존재할 경우 정보 업데이트
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(existingUser
                .update(oAuth2UserInfo.getName(), oAuth2UserInfo.getProfile())
        );
    }
}
