package com.yapp18.retrospect.mapper;

import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.PostDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel="spring", uses = {TagMapper.class, LikeMapper.class, SecurityContextHolder.class})
public interface PostMapper {
    PostMapper instance = Mappers.getMapper(PostMapper.class);


    // entity -> listResponseDto : entity에서 값 조회해서 dto로 넣기
    @Mapping(target ="nickname", expression= "java(post.getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(post.getUser().getProfile())")
    @Mapping(target = "commentCnt", expression = "java((long)post.getComments().size())")
    @Mapping(target = "scrapCnt", expression = "java((long)post.getLike().size())")
    @Mapping(target = "createdAt", expression = "java(post.getCreatedAt())")
    @Mapping(target = "scrap", expression = "java(isScrap(post.getLike(),userIdx))")
    PostDto.ListResponse postToListResponse(Post post, Long userIdx);

    // 상세보기
    @Mapping(target = "templateIdx", expression = "java(post.getTemplate().getTemplateIdx())")
    @Mapping(target ="nickname", expression= "java(post.getUser().getNickname())")
    @Mapping(target = "profile", expression = "java(post.getUser().getProfile())")
    @Mapping(target = "commentCnt", expression = "java((long)post.getComments().size())")
    @Mapping(target = "createdAt", expression = "java(post.getCreatedAt())")
    PostDto.detailResponse postToDetailResponse(Post post);

    // 수정하기
//    Post updateToPost(PostDto.updateRequest request);

    // dto -> entity

    // 스크랩 여부 판별
    default boolean isScrap(List<Like> likeList, Long userIdx){
        if (likeList.isEmpty()) return false;
        return likeList.stream().map(like -> like.getUser().getUserIdx()).collect(Collectors.toList())
                    .contains(userIdx);

    }
}
