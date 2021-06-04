package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.web.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // 스프링 컨테이너에 객체로 관리
public interface CommentMapper {
    CommentMapper instance = Mappers.getMapper(CommentMapper.class);

    CommentDto.ListResponse commentToListResponse(Comment comment);

//    @Mapping(target = "userIdx", expression = "java((long)userIdx)")
//    Comment commentRequestToEntity(CommentDto.CommentRequest commentRequest, Long userIdx);
}
