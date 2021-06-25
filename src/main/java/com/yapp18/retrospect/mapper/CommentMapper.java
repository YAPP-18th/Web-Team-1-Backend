package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface CommentMapper {
    CommentMapper instance = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "isWriter", target = "writer")
    CommentDto.BasicResponse commentToBasicResponse(Comment comment, boolean isWriter);

    @Mapping(target = "writer", ignore = true)
    CommentDto.BasicResponse commentToBasicResponse(Comment comment);

    @Mapping(target = "commentIdx", ignore = true)
    @Mapping(target = "comments", expression = "java(inputRequest.getComments())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Comment toEntity(CommentDto.InputRequest inputRequest, User user, Post post);
//    @Mapping(target = "userIdx", expression = "java((long)userIdx)")
//    Comment commentRequestToEntity(CommentDto.CommentRequest commentRequest, Long userIdx);
}
