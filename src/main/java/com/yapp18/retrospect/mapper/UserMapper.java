package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    UserDto.ProfileResponse userToProfileResponse(User user);

    @Mapping(target = "email", expression = "java(user.getEmail())") // 3
    @Mapping(target = "name", expression = "java(user.getName())") // 3
    @Mapping(target = "provider", expression = "java(user.getProvider())") // 3
    @Mapping(target = "providerId", expression = "java(user.getProviderId())")
    @Mapping(target = "role", expression = "java(user.getRole())") // 3
    @Mapping(target = "nickname", expression = "java(updateRequest.getNickname())") // 3
    @Mapping(target = "intro", expression = "java(updateRequest.getIntro())") // 3
    @Mapping(target = "profile", expression = "java(updateRequest.getProfile())") // 3
    @Mapping(target = "job", expression = "java(updateRequest.getJob())") // 3
    User updateRequestToUser(User user, UserDto.UpdateRequest updateRequest);
}
