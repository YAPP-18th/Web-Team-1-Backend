package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.service.ListService;
import com.yapp18.retrospect.web.dto.MypageDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel="spring", uses = {TagMapper.class})
public interface PostMapper{
    PostMapper instance = Mappers.getMapper(PostMapper.class);

    // entity -> listResponseDto : entity에서 값 조회해서 dto로 넣기
//    @Mapping(target ="nickname", expression= "java(post.getUser().getNickname())")
//    @Mapping(target = "profile", expression = "java(post.getUser().getProfile())")
//    @Mapping(target = "tag", source = "tag")
//    List<MypageDto> postToListResponse(Stream<Post> post);

//    Post updateToPost();

}