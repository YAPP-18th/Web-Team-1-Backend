package com.yapp18.retrospect.domain.post;

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
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @AfterEach // JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드
    public void teardown() {
        postRepository.deleteAll();
    }

    String name = "테스트 이름";
    String nickname = "테스트 닉네임";
    String intro = "테스트 자기소개";
    String email = "test@gmail.com";
    String picture = null;
    String platform = "google";
    String job = "개발자";
    String access_token = null;

    User user = User.builder()
            .name(name)
            .nickname(nickname)
            .intro(intro)
            .email(email)
            .picture(picture)
            .platform(platform)
            .job(job)
            .access_token(access_token)
            .build();

    String template_name = "4F";
    String template_content = "...";

    Template template = Template.builder()
            .template_name(template_name)
            .template(template_content)
            .user(user)
            .build();

    @Test
    public void 게시글저장_불러오기(){
        String category = "개발";
        String title = "테스트 제목";
        String contents = "테스트 콘텐츠";
        long view = 0;

        //given
        postRepository.save(Post.builder()
                .category(category)
                .title(title)
                .contents(contents)
                .view(view)
                .user(user)
                .template(template)
                .build());

        //when
        List<Post> postList = postRepository.findAll();

        Post post = postList.get(0);

        //then
        assertThat(post.getCategory()).isEqualTo(category);
        assertThat(post.getTemplate().getTemplate_name()).isEqualTo(template_name);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContents()).isEqualTo(contents);
        assertThat(post.getView()).isEqualTo(view);
    }
}
