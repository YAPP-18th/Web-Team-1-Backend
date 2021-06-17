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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AppProperties appProperties;
    private final UserMapper userMapper;

    @Value("${app.values.s3ProfileImagePathSuffix}")
    public String s3ProfileImagePathSuffix;

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
    @Transactional
    public UserDto.ProfileResponse getUserProfiles(Long userIdx) {
        return userRepository.findByUserIdx(userIdx)
                .map(mapper::userToProfileResponse)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
    }

    // 회원 프로필 정보 업데이트
    @Transactional
    public UserDto.ProfileResponse updateUserProfiles(Long userIdx, UserDto.UpdateRequest request){
        User user = userRepository.findByUserIdx(userIdx)
                .map(existingUser -> {
                    if(!existingUser.getProfile().equals(request.getProfile())) { // 수정사항이 있다
                        List<String> list = Arrays.asList(request.getProfile());
                        imageService.deleteImageList(list, userIdx, s3ProfileImagePathSuffix); // list에 없는 s3 가비지 데이터를 추출하여 삭제
                    }
                    return existingUser.updateProfile(request.getProfile(), request.getName(), request.getNickname(), request.getJob(), request.getIntro());
                })
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
        userRepository.save(user);
        return mapper.userToProfileResponse(user);
    }

    @Transactional
    public void deleteUser(Long userIdx){
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
        userRepository.delete(user);
        imageService.deleteUserInfo(userIdx); // s3에 해당 사용자 정보 모두 삭제
    }

    public boolean findUserByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }
}
