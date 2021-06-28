package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface CommentMapper extends GenericMapper<Comment, CommentDto>{
    CommentMapper instance = Mappers.getMapper(CommentMapper.class);

    @Override
    @Mapping(target = "userIdx", expression = "java(comment.getUser().getUserIdx())")
    @Mapping(target = "nickname", expression = "java(comment.getUser().getNickname())")
    CommentDto.BasicResponse toDto(Comment comment);

    @Mapping(target = "writer", expression = "java(comment.isWriter(user))")
    @Mapping(target = "userIdx", expression = "java(comment.getUser().getUserIdx())")
    @Mapping(target = "nickname", expression = "java(comment.getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(comment.getUser().getProfile())")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt())")
    @Mapping(target = "modifiedAt", expression = "java(comment.getModifiedAt())")
    CommentDto.ListResponse toDto(Comment comment, User user);

    @Mapping(target = "commentIdx", ignore = true)
    @Mapping(target = "comments", expression = "java(inputRequest.getComments())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Comment toEntity(CommentDto.InputRequest inputRequest, User user, Post post);

    @Mapping(target = "commentIdx", source = "commentIdx")
    @Mapping(target = "comments", expression = "java(commentDto.getComments())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Comment toEntity(CommentDto commentDto, User user, Long commentIdx);
}
