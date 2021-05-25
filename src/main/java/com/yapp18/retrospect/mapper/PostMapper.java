package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.web.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper{

//    @Mapping(source="post.user.profile", target="profile")
//    PostDto.detailResponse toDto(Post post);
}