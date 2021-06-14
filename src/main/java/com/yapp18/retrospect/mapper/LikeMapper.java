package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.web.dto.LikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface LikeMapper {
    LikeMapper instance = Mappers.getMapper(LikeMapper.class);

    @Mapping(target ="nickname", expression= "java(post.getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(post.getUser().getProfile())")
    @Mapping(target = "commentCnt", expression = "java((long)post.getComments().size())")
    @Mapping(target = "scrapCnt", expression = "java((long)post.getLike().size())")
    @Mapping(target = "createdAt", expression = "java(like.getCreatedAt())")
    LikeDto.BasicResponse likeToBasicResponse(Like like, Post post);
}
