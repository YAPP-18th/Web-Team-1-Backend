//package com.yapp18.retrospect.domain.post;
//
//
//import com.yapp18.retrospect.service.ImageService;
//import com.yapp18.retrospect.web.controller.PostController;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.jws.HandlerChain;
//import javax.mail.Multipart;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@ExtendWith(SpringExtension.class) // @Runwith
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.yaml")
//public class PostsTest {
//
//    // 회고글 작성 시 image를 s3에 업로드한다.
//    // 최종 imageUrl들과 s3 url들을 비교한다.
//    @Autowired
//    private ImageService imageService;
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    @Autowired
//    private MockMvc mvc;
//
//    @BeforeEach
//    void 이미지_저장() throws Exception {
//        // s3에 업로드하고
//        MockMultipartFile file = new MockMultipartFile("file","filename-1.jpeg", "image/jpeg", "some-image".getBytes());
//        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//
//    }
//
//
//    @Test
//    void 글_저장할_때_이미지_처리(){
//
//    }
//
//
//
//
//
//}
