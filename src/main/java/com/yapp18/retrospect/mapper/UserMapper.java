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

    @Mapping(target = "mine",  constant = "true")
    UserDto.ProfileResponse userToProfileResponse(User user);

    @Mapping(target = "mine",  expression = "java(mine)")
    UserDto.ProfileResponse userToProfileResponse(User user, boolean mine);
}
