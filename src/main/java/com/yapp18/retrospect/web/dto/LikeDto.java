package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class LikeDto {
    @Getter
    @Setter
    @ApiModel(value = "회고글 스크랩 등록", description = "회고글 스크랩 등록 요청 모델")
    public static class InputRequest {
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

        public Like toEntity(Post post, User user){
            return Like.builder()
                    .post(post)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "회고글 스크랩 조회", description = "회고글 스크랩 응답 모델")
    public static class BasicResponse {
        @ApiModelProperty(value = "스크랩 idx")
        private Long likeIdx;

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
    }
}
