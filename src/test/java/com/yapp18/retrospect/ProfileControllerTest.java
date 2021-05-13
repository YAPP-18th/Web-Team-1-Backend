//package com.yapp18.retrospect;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public class ProfileControllerTest {
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void Profile확인(){
//        //when
//        String profile = this.restTemplate.getForObject("/profile", String.class);
//        //then
//        assertThat(profile).isEqualTo("local");
//    }
//}
