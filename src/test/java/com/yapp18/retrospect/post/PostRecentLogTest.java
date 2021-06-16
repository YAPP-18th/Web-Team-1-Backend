//package com.yapp18.retrospect.post;
//
//
//import com.yapp18.retrospect.config.ErrorInfo;
//import com.yapp18.retrospect.config.SecurityConfig;
//import com.yapp18.retrospect.domain.post.Post;
//import com.yapp18.retrospect.domain.post.PostRepository;
//import com.yapp18.retrospect.domain.recent.RecentLog;
//import com.yapp18.retrospect.domain.recent.RecentRepository;
//import com.yapp18.retrospect.security.TokenAuthenticationFilter;
//import com.yapp18.retrospect.web.advice.EntityNullException;
//import com.yapp18.retrospect.web.controller.AuthController;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = AuthController.class,excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenAuthenticationFilter.class) })
//public class PostRecentLogTest {
//
//    @Autowired private MockMvc mvc;
//
//    @Autowired
//    private RecentRepository recentRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @WithMockUser(roles="MEMBER")
//    @Test
//    public void Redis_테스트(){
//        // postIdx 로 해당 post 찾기
//        Long userIdx = 2L;
//        Post post = postRepository.findById(73L)
//                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
//        RecentLog result = RecentLog.builder().userIdx(userIdx).post(post).build();
//        recentRepository.save(result);
//
//        RecentLog findMyRecentPosts = recentRepository.findById(userIdx).get();
//        assertThat(findMyRecentPosts.getPost()).isEqualTo(post);
//        assertThat(findMyRecentPosts.getUserIdx()).isEqualTo(userIdx);
//
//    }
//
//
//
//}
//
