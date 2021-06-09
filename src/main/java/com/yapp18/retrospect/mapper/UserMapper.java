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

    @Mapping(target = "email", constant = "") // 3
    @Mapping(target = "name", constant = "") // 3
    @Mapping(target = "provider", expression = "java(AuthProvider.empty)") // 3
    @Mapping(target = "providerId", constant = "") // 3
    @Mapping(target = "role", expression = "java(Role.GUEST)") // 3
    User updateRequestToUser(UserDto.UpdateRequest updateRequest);
}
