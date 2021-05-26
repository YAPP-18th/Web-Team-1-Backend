package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.ProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);
    User updateRequestToEntity(ProfileDto.UpdateRequest updateRequest);
    ProfileDto.ProfileResponse userToProfileResponse(User user);
//    User profileDtoToUser(ProfileDto dto);
}
