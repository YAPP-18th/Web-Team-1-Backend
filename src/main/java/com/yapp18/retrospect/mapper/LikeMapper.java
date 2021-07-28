package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.LikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface LikeMapper extends GenericMapper<Like, LikeDto.BasicResponse> {
    LikeMapper instance = Mappers.getMapper(LikeMapper.class);

    @Override
    Like toEntity(LikeDto.BasicResponse dto);

    @Mapping(target = "likeIdx", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Like toEntity(User user, Post post);

    @Override
    @Mapping(target = "likeIdx", expression= "java(like.getLikeIdx())")
    @Mapping(target = "postIdx", expression= "java(like.getPost().getPostIdx())")
    @Mapping(target = "nickname", expression= "java(like.getPost().getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(like.getPost().getUser().getProfile())")
    @Mapping(target = "title", expression= "java(like.getPost().getTitle())")
    @Mapping(target = "category", expression= "java(like.getPost().getCategory())")
    @Mapping(target = "contents", expression= "java(like.getPost().getContents())")
    @Mapping(target = "tagList", expression = "java(like.getPost().getTagList())")
    @Mapping(target = "view", expression = "java(like.getPost().getView())")
    @Mapping(target = "commentCnt", expression = "java((long)like.getPost().getComments().size())")
    @Mapping(target = "scrapCnt", expression = "java((long)like.getPost().getLike().size())")
    @Mapping(target = "createdAt", expression = "java(like.getCreatedAt())")
    LikeDto.BasicResponse toDto(Like like);
}
