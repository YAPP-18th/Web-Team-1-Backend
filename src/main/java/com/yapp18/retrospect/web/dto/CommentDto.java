package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    @Getter
    public static class InputRequest {
        @ApiModelProperty(value = "회고글 idx")
        private final Long postIdx;
        @ApiModelProperty(value = "댓글 내용")
        private final String comments;

        @Builder
        public InputRequest(Long postIdx, String comments) {
            this.postIdx = postIdx;
            this.comments = comments;
        }

        public Comment toEntity(Post post, User user){
            return Comment.builder()
                    .comments(this.comments)
                    .post(post)
                    .user(user)
                    .build();
        }
    }

    @Getter
    public static class UpdateRequest {
        @ApiModelProperty(value = "댓글 내용")
        private final String comments;
        @ApiModelProperty(value = "회고글 idx")
        private final Long postIdx;

        @Builder
        public UpdateRequest(String comments, Long postIdx) {
            this.comments = comments;
            this.postIdx = postIdx;
        }

        public Comment toEntity(User user, Post post){
            return Comment.builder()
                    .comments(comments)
                    .user(user)
                    .post(post)
                    .build();
        }
    }

    @Getter
    public static class ListResponse<T> {
        @ApiModelProperty(value = "작성자 여부")
        private final boolean isWriter;
        @ApiModelProperty(value = "댓글 내용")
        private T result;

        @Builder
        public ListResponse(boolean isWriter, T result) {
            this.isWriter = isWriter;
            this.result = result;
        }
    }

    @Getter
    public static class BasicResponse {
        @ApiModelProperty(value = "댓글 idx")
        private final Long commentIdx;

        @ApiModelProperty(value = "내용")
        private final String comments;

        @JsonIgnore
        @ApiModelProperty(value = "작성자")
        private final User user;

        @ApiModelProperty(value = "작성자 idx")
        private final Long userIdx;

        @ApiModelProperty(value = "작성자 닉네임")
        private final String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private final String profile;

        @ApiModelProperty(value = "작성자 여부")
        private final boolean writer;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private final LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
        @ApiModelProperty(value = "수정날짜")
        private final LocalDateTime modifiedAt;

        @Builder
        public BasicResponse(Long commentIdx, String comments, User user, boolean writer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.commentIdx = commentIdx;
            this.comments = comments;
            this.user = user;
            this.userIdx = user.getUserIdx();
            this.nickname = user.getNickname();
            this.profile = user.getProfile();
            this.writer = writer;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
