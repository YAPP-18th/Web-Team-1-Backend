package com.yapp18.retrospect.domain.like;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LikeRepositoryTest {
    @Autowired
    LikeRepository likeRepository;

    @AfterEach
    public void teardown() {
        likeRepository.deleteAll();
    }

    String name_1 = "테스트 이름1";
    String nickname_1 = "테스트 닉네임1";
    String intro_1 = "테스트 자기소개1";
    String email_1 = "test1@gmail.com";
    String picture_1 = null;
    String platform_1 = "google";
    String job_1 = "개발자";
    String access_token_1 = null;

    String name_2 = "테스트 이름2";
    String nickname_2 = "테스트 닉네임2";
    String intro_2 = "테스트 자기소개2";
    String email_2 = "test2@naver.com";
    String picture_2 = null;
    String platform_2 = "naver";
    String job_2 = "기획자";
    String access_token_2 = null;

    User user1 = User.builder()
            .name(name_1)
            .nickname(nickname_1)
            .intro(intro_1)
            .email(email_1)
            .picture(picture_1)
            .platform(platform_1)
            .job(job_1)
            .build();


    User user2 = User.builder()
            .name(name_2)
            .nickname(nickname_2)
            .intro(intro_2)
            .email(email_2)
            .picture(picture_2)
            .platform(platform_2)
            .job(job_2)
            .build();

    String template_name = "4F";
    String template_content = "...";

    Template template = Template.builder()
            .template_name(template_name)
            .template(template_content)
            .user(user2)
            .build();

    String category = "개발";
    String title = "테스트 제목";
    String content = "테스트 콘텐츠";
    long view = 0;

    Post post = Post.builder()
            .category(category)
            .title(title)
            .contents(content)
            .view(view)
            .user(user2)
            .template(template)
            .build();

    @Test
    public void 스크랩저장_불러오기() {
        likeRepository.save(Like.builder()
                .user(user1)
                .post(post)
                .build()
        );

        List<Like> likeList = likeRepository.findAll();

        Like like = likeList.get(0);

        assertThat(like.getUser().getName()).isEqualTo(name_1);
        assertThat(like.getPost().getUser().getName()).isEqualTo(name_2);
        assertThat(like.getPost().getTitle()).isEqualTo(title);
    }
}
