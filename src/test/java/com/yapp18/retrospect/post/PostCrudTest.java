//package com.yapp18.retrospect.post;
//
//import com.yapp18.retrospect.domain.post.Post;
//import com.yapp18.retrospect.domain.post.PostQueryRepository;
//import com.yapp18.retrospect.domain.post.PostRepository;
//import com.yapp18.retrospect.domain.template.Template;
//import com.yapp18.retrospect.domain.template.TemplateRepository;
//import com.yapp18.retrospect.domain.user.Role;
//import com.yapp18.retrospect.domain.user.User;
//import com.yapp18.retrospect.domain.user.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.awt.font.OpenType;
//import java.util.Optional;
//
//@DataJpaTest
//public class PostCrudTest {
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    TemplateRepository templateRepository;
//
//    @BeforeEach
//    void setUp(){
//        // user 설정
//        User user1 = User.builder()
//                .name("허정민").nickname("tape22").intro("자기소개.").job("백수")
//                .email("test@naver.com").profile("profile_url")
//                .provider("google").providerId("13453252535").role(Role.MEMBER).build();
//        // template 설정
//        Template template = Template.builder().template("템플릿").templateName("4F").build();
//
//        userRepository.save(user1);
//        templateRepository.save(template);
//
//        System.out.println(user1.getUserIdx());
//        System.out.println(template.getTemplateIdx());
//
//    }
//
//    @Test
//    @DisplayName("회고글 목록 조회: 최신순")
//    void getRecentPostsList(){
//        // given
////        User user = userRepository.findById(1L);
////        Post post = Post.builder()
////                .category("design")
////                .contents("내용.......!!!!")
////                .title("테스트 1 제목")
////                .user(user)
////                .build();
//
//        // when
//        // then
//        System.out.println("test...");
//    }
//}
