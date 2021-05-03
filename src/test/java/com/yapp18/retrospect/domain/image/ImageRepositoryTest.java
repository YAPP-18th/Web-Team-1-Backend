package com.yapp18.retrospect.domain.image;

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
public class ImageRepositoryTest {
    @Autowired
    ImageRepository imageRepository;

    @AfterEach
    public void teardown() {
        imageRepository.deleteAll();
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

    String category = "개발";
    String title = "테스트 제목";
    String contents = "테스트 콘텐츠";
    long view = 0;

    Post post = Post.builder()
            .category(category)
            .title(title)
            .contents(contents)
            .view(view)
            .user(user)
            .template(template)
            .build();

    @Test
    public void 이미지저장_불러오기() {
        //given
        String image_url = "https://t1.daumcdn.net/liveboard/rmsdhf/2f08f718bab6497d972f10999b0b31ce.png";

        imageRepository.save(Image.builder()
                .post(post)
                .image_url(image_url)
                .build()
        );

        //when
        List<Image> imageList = imageRepository.findAll();

        Image image = imageList.get(0);

        //then
        assertThat(image.getPost().getUser().getName()).isEqualTo(name);
        assertThat(image.getPost().getTitle()).isEqualTo(title);
        assertThat(image.getImage_url()).isEqualTo(image_url);
    }
}
