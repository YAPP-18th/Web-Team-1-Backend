package com.yapp18.retrospect.common;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;

import java.time.LocalDateTime;

public class EntityCreator {
    private static final Long USER_IDX = 1L;
    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;
    private static final Long TEMPLATE_IDX = 1L;

    public static User createUserEntity(){
        User user = User.builder()
                .userIdx(USER_IDX)
                .email("test@example.com")
                .name("테스트이름")
                .nickname("테스트닉네임")
                .profile("프로필URL")
                .provider(AuthProvider.kakao)
                .providerId("1")
                .role(Role.MEMBER)
                .job("테스트직업")
                .intro("테스트자기소개")
                .build();

        user.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));
        user.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 1, 0));

        return user;
    }

    public static Post createPostEntity(){
        Post post =  Post.builder()
                .postIdx(POST_IDX)
                .title("회고글제목")
                .category("카테고리")
                .contents("글내용")
                .user(createUserEntity())
                .template(createTemplateEntity())
                .build();

        post.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));
        post.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));

        return post;
    }

    public static Template createTemplateEntity(){
        Template template = Template.builder()
                .templateIdx(TEMPLATE_IDX)
                .templateName("템플릿이름")
                .template("템플릿내용")
                .build();

        template.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));
        template.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));

        return template;
    }

    public static Comment createCommentEntity(){
        Comment comment = Comment.builder()
                .commentIdx(COMMENT_IDX)
                .post(createPostEntity())
                .user(createUserEntity())
                .comments("댓글내용")
                .build();

        comment.setCreatedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));
        comment.setModifiedAt(LocalDateTime.of(2021, 10, 28, 12, 0, 0));

        return comment;
    }
}
