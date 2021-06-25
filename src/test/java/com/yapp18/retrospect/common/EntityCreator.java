package com.yapp18.retrospect.common;

import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;

public class EntityCreator {
    private static final Long USER_IDX = 1L;
    private static final Long POST_IDX = 1L;
    private static final Long COMMENT_IDX = 1L;
    private static final Long TEMPLATE_IDX = 1L;

    public static User createUserEntity(){
        return User.builder()
                .userIdx(USER_IDX)
                .email("test@example.net")
                .name("테스트이름")
                .nickname("테스트닉네임")
                .profile("profile-url")
                .provider(AuthProvider.kakao)
                .providerId("12345")
                .role(Role.MEMBER)
                .job("테스트직업")
                .intro("테스트자기소개")
                .build();
    }

    public static Post createPostEntity(){
        return Post.builder()
                .postIdx(POST_IDX)
                .title("회고글제목")
                .category("카테고리")
                .contents("글내용")
                .user(createUserEntity())
                .template(createTemplateEntity())
                .build();
    }

    public static Template createTemplateEntity(){
        return Template.builder()
                .templateIdx(TEMPLATE_IDX)
                .templateName("템플릿이름")
                .template("템플릿내용")
                .build();
    }

    public static Comment createCommentEntity(){
        return Comment.builder()
                .commentIdx(COMMENT_IDX)
                .post(createPostEntity())
                .user(createUserEntity())
                .comments("댓글내용")
                .build();
    }
}
