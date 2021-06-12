package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.UserMapper;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.security.oauth2.user.OAuth2UserInfo;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final UserMapper userMapper;

    // DB에 존재하지 않을 경우 회원 가입
    @Transactional
    public User registerNewUser(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        User user = userRepository.save(User.builder()
                .role(Role.MEMBER)
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profile(appProperties.getValues().getDefaultProfileUrl())
                .provider(AuthProvider.valueOf(registrationId))
                .providerId(oAuth2UserInfo.getId())
                .build()
        );

        user.updateNickname(user.getUserIdx() +
                appProperties.getValues().getDefaultNicknameSuffix());

        return user;
    }

    // userId로 회원 프로필 정보 조회
    public UserDto.ProfileResponse getUserProfiles(Long userIdx) {
        return userRepository.findByUserIdx(userIdx)
                .map(mapper::userToProfileResponse)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
    }

    // 회원 프로필 정보 업데이트
    public UserDto.ProfileResponse updateUserProfiles(Long userIdx, UserDto.UpdateRequest request){
        User user = userRepository.findByUserIdx(userIdx)
                .map(existingUser -> existingUser.updateProfile(request.getProfile(), request.getName(), request.getNickname(), request.getJob(), request.getIntro()))
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
        userRepository.save(user);
        return mapper.userToProfileResponse(user);
    }

    public boolean findUserByNickname(String nickname){
        User user = userRepository.findByNickname(nickname);
        return user != null;
    }
}
