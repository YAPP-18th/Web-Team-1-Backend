package com.yapp18.retrospect.domain.scrap;

import com.yapp18.retrospect.domain.post.Post;
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
public class ScrapRepositoryTest {
    @Autowired
    ScrapRepository scrapRepository;

    @AfterEach
    public void teardown() {
        scrapRepository.deleteAll();
    }

    String name_1 = "테스트 이름1";
    String email_1 = "test1@gmail.com";
    String profile_url_1 = null;
    String platform_1 = "google";
    String access_token_1 = null;

    String name_2 = "테스트 이름";
    String email_2 = "test2@naver.com";
    String profile_url_2 = null;
    String platform_2 = "naver";
    String access_token_2 = null;

    User user1 = User.builder()
            .name(name_1)
            .email(email_1)
            .profile_url(profile_url_1)
            .platform(platform_1)
            .access_token(access_token_1)
            .build();


    User user2 = User.builder()
            .name(name_2)
            .email(email_2)
            .profile_url(profile_url_2)
            .platform(platform_2)
            .access_token(access_token_2)
            .build();

    String category = "개발";
    String template = "DAKI";
    String title = "테스트 제목";
    String content = "테스트 콘텐츠";
    long view = 0;

    Post post = Post.builder()
            .user(user2)
            .category(category)
            .template(template)
            .title(title)
            .content(content)
            .view(view)
            .build();

    @Test
    public void 스크랩저장_불러오기() {
        scrapRepository.save(Scrap.builder()
                .user(user1)
                .post(post)
                .build()
        );

        List<Scrap> scrapList = scrapRepository.findAll();

        Scrap scrap = scrapList.get(0);

        assertThat(scrap.getUser().getName()).isEqualTo(name_1);
        assertThat(scrap.getPost().getUser().getName()).isEqualTo(name_2);
        assertThat(scrap.getPost().getTitle()).isEqualTo(title);
    }
}
