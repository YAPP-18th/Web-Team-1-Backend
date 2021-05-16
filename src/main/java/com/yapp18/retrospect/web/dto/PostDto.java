package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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

        @ApiModelProperty(value = "내용 ")
        private String contents;

        @ApiModelProperty(value = "작성자 idx")
        private Long userIdx;

        @ApiModelProperty(value = "적용한 템플릿 idx")
        private Long templateIdx;


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

}
