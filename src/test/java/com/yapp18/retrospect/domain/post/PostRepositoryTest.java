package com.yapp18.retrospect.domain.post;

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
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @AfterEach // JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드
    public void teardown() {
        postRepository.deleteAll();
    }

    String name = "테스트 이름";
    String email = "test@gmail.com";
    String profile_url = null;
    String platform = "google";
    String access_token = null;

    User user = User.builder()
            .name(name)
            .email(email)
            .profile_url(profile_url)
            .platform(platform)
            .access_token(access_token)
            .build();

    @Test
    public void 게시글저장_불러오기(){
        String category = "개발";
        String template = "DAKI";
        String title = "테스트 제목";
        String content = "테스트 콘텐츠";
        long view = 0;

        //given
        postRepository.save(Post.builder()
                .user(user)
                .category(category)
                .template(template)
                .title(title)
                .content(content)
                .view(view)
                .build());

        //when
        List<Post> postList = postRepository.findAll();

        Post post = postList.get(0);

        //then
        assertThat(post.getCategory()).isEqualTo(category);
        assertThat(post.getTemplate()).isEqualTo(template);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getView()).isEqualTo(view);
    }
}
