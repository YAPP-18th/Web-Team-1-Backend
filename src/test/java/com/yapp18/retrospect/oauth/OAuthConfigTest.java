package com.yapp18.retrospect.oauth;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // 고정된 포트(8080)으로 사용
@TestPropertySource(properties =  "spring.config.location=classpath:/application-oauth.yaml") // Junit 테스트시에도 설정으로 적용
public class OAuthConfigTest {
//    @Before
//    public void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
//    }
}
