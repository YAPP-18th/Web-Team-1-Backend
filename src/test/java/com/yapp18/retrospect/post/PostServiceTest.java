package com.yapp18.retrospect.post;

import com.yapp18.retrospect.common.EntityCreator;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.awt.font.OpenType;
import java.util.List;
import java.util.Optional;

//@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TemplateRepository templateRepository;

    @BeforeEach
    void setUp(){
//        // user 설정
//        User user1 = User.builder()
//                .name("허정민").nickname("tape22").intro("자기소개.").job("백수")
//                .email("test@naver.com").profile("profile_url")
//                .provider(AuthProvider.google).providerId("13453252535").role(Role.MEMBER).build();
//        // template 설정
//        Template template = Template.builder().template("템플릿").templateName("4F").build();
//
//        userRepository.save(user1);
//        templateRepository.save(template);
//
//        System.out.println(user1.getUserIdx());
//        System.out.println(template.getTemplateIdx());
    }

    @Test
    void 회고글이_없는_경우(){
        // list로 글 읽어오기
        List<Post> posts = postRepository.findAllByOrderByViewDesc(PageRequest.of(0,10));
        System.out.println(posts);

        // when

        // 없으면 빈 배열? 아니면 exception?

    }
}
