package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    User updateRequestToEntity(UserDto.UpdateRequest updateRequest);
    UserDto.ProfileResponse userToProfileResponse(User user);
}
