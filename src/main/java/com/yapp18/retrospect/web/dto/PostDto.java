package com.yapp18.retrospect.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostDto {

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

        @ApiModelProperty(value = "작성자 idx")
        private Long userIdx;

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

    @Data
    @NoArgsConstructor
    @ApiModel(value = "회고글 목록 조회(조회순)", description = "회고글 목록 조회 모델")
    // post 조회
    public static class ListResponse{
        // postIdx 넘겨줘야함.
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

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
        private String tag;

        @ApiModelProperty(value = "조회수")
        private int view;

        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime created_at;

        @ApiModelProperty(value = "댓글 수")
        private Long commentCnt;

        @ApiModelProperty(value = "스크랩 수 ")
        private Long scrapCnt;


        @QueryProjection
        public ListResponse(Long postIdx,String title, String category, String contents, String nickname,
                            String profile, String  tag, LocalDateTime created_at, int view,
                            Long commentCnt, Long scrapCnt){
            this.postIdx = postIdx;
            this.title =  title;
            this.category = category;
            this.contents = contents;
            this.nickname = nickname;
            this.profile = profile;
            this.tag = tag;
            this.created_at = created_at;
            this.view = view;
            this.commentCnt = commentCnt;
            this.scrapCnt = scrapCnt;

        }


    }

}
