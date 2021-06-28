package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @ApiModelProperty(value = "댓글 내용")
    private String comments;

    @SuperBuilder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "댓글 등록", description = "댓글 등록 요청 모델")
    public static class InputRequest extends CommentDto {
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;
    }

    @SuperBuilder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "댓글 리스트 조회", description = "댓글 리스트 응답 모델")
    public static class ListResponse extends CommentDto {
        @ApiModelProperty(value = "댓글 idx")
        private Long commentIdx;

        @ApiModelProperty(value = "작성자 idx")
        private Long userIdx;

        @ApiModelProperty(value = "작성자 닉네임")
        private String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private String profile;

        @ApiModelProperty(value = "작성자 여부")
        private boolean writer;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "수정날짜")
        private LocalDateTime modifiedAt;
    }

    @SuperBuilder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "댓글 조회", description = "댓글 응답 모델")
    public static class BasicResponse extends CommentDto {
        @ApiModelProperty(value = "댓글 idx")
        private Long commentIdx;

        @ApiModelProperty(value = "작성자 idx")
        private Long userIdx;

        @ApiModelProperty(value = "작성자 닉네임")
        private String nickname;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "수정날짜")
        private LocalDateTime modifiedAt;
    }
}
