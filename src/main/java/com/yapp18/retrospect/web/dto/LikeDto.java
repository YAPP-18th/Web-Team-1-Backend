package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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

//    @Getter
//    @Setter
//    @ApiModel(value = "회고글 스크랩 등록", description = "스크랩 한 회고글 기본 응답 모델")
//    public static class BasicResponse {
//        @ApiModelProperty(value = "회고글 idx")
//        private String postIdx;
//    }
}
