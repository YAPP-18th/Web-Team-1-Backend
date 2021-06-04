package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentDto {

    @Getter
    public static class CommentRequest {
        @ApiModelProperty(value = "회고글 idx")
        private final Long postIdx;
        @ApiModelProperty(value = "내용")
        private final String comments;

        @Builder
        public CommentRequest(Long postIdx, String comments) {
            this.postIdx = postIdx;
            this.comments = comments;
        }

        public Comment toEntity(User user, Post post){
            return Comment.builder()
                    .comment(this.comments)
                    .user(user)
                    .post(post)
                    .build();
        }
    }

    @Getter
    public static class ListResponse {
        @ApiModelProperty(value = "댓글 idx")
        private final Long commentIdx;

        @ApiModelProperty(value = "내용")
        private final String comments;

        @ApiModelProperty(value = "작성자 닉네임 ")
        private final String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private final String profile;

        @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private final LocalDateTime createdAt;

        @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
        @ApiModelProperty(value = "수정날짜")
        private final LocalDateTime modifiedAt;

        @Builder
        public ListResponse(Long commentIdx, String comments, String nickname, String profile, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.commentIdx = commentIdx;
            this.comments = comments;
            this.nickname = nickname;
            this.profile = profile;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
