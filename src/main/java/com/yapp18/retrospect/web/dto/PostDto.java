package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ListResponse {

        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

        @ApiModelProperty(value = "회고글 제목")
        private String title;

        @ApiModelProperty(value = "카테고리")
        private String category;

        @ApiModelProperty(value = "내용")
        private String contents;

        @ApiModelProperty(value = "작성자 닉네임 ")
        private String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private String profile;

        @ApiModelProperty(value = "키워드 ")
        private List<TagDto.FirstTagDto> tagList;

        @ApiModelProperty(value = "조회수")
        private int view;

        @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime createdAt;

        @ApiModelProperty(value = "댓글 수")
        private Long commentCnt;

        @ApiModelProperty(value = "스크랩 수 ")
        private Long scrapCnt;

        @ApiModelProperty(value = "스크랩 여부")
        private boolean scrap;

    }


    // post 저장
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value="회고글 저장 ", description="회고글을 저장하는 모델")
    public static class saveResponse{
        @ApiModelProperty(value = "회고글 제목")
        private String title;

        @ApiModelProperty(value = "카테고리")
        private String category;

        @ApiModelProperty(value = "내용")
        private String contents;

        @ApiModelProperty(value = "적용한 템플릿 idx")
        private Long templateIdx;

        @ApiModelProperty(value = "이미지")
        private List<String> image;

        @ApiModelProperty(value = "태그 String 담긴 배열 ")
        private List<String> tag;

        @Builder
        public Post toEntity(User user, Template template){
            return Post.builder()
                    .title(title)
                    .category(category)
                    .contents(contents)
                    .user(user)
                    .template(template)
                    .build();
        }
    }


    @Getter
    @Setter
    @ApiModel(value = "회고글 수정하기 ", description = "회고글 목록 수정  모델")
    public static class updateRequest {
        @ApiModelProperty(value = "카테고리")
        private String category;
        @ApiModelProperty(value = "제목 ")
        private String title;
        @ApiModelProperty(value = "내용 ")
        private String contents;
        @ApiModelProperty(value = "태그")
        private List<String> tagList;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "회고글 상세조회", description = "회고글 상세조회 response 값 ")
    public static class detailResponse{
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

        @ApiModelProperty(value = "템플릿 idx")
        private Long templateIdx;

        @ApiModelProperty(value = "회고글 제목")
        private String title;

        @ApiModelProperty(value = "카테고리")
        private String category;

        @ApiModelProperty(value = "내용 ")
        private String contents;

        @ApiModelProperty(value = "작성자 닉네임 ")
        private String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private String profile;

        @ApiModelProperty(value = "키워드 ")
        private List<TagDto> tagList;

        @ApiModelProperty(value = "조회수")
        private int view;

        @JsonFormat(pattern = "MMM dd,yyyy",locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime createdAt;

        @ApiModelProperty(value = "댓글 수")
        private Long commentCnt;

//        @ApiModelProperty(value = "스크랩 여부")
//        private boolean isScrap;
    }

}
