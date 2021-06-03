package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.web.dto.PostListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring", uses = {TagMapper.class})
public interface PostMapper{
    PostMapper instance = Mappers.getMapper(PostMapper.class);

    // entity -> listResponseDto : entity에서 값 조회해서 dto로 넣기
    @Mapping(target ="nickname", expression= "java(post.getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(post.getUser().getProfile())")
    @Mapping(target = "commentCnt", expression = "java((long)post.getComments().size())")
    @Mapping(target = "scrapCnt", expression = "java((long)post.getLike().size())")
    @Mapping(target = "createdAt", expression = "java(post.getCreatedAt())")
    PostListDto postToListResponse(Post post);

    // dto -> entity


}
