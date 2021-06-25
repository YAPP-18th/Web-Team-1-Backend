package com.yapp18.retrospect.api.oauth2;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.AbstractControllerTest;
import org.springframework.boot.test.mock.mockito.MockBean;

public class OAuth2ControllerTest extends AbstractControllerTest {
    private final User newUser = User.builder()
            .email("ybell1028@daum.net")
            .name("real-name")
            .nickname("Walkman")
            .profile("profile-url")
            .provider(AuthProvider.kakao)
            .providerId("12345")
            .role(Role.MEMBER)
            .job("job")
            .intro("intro")
            .build();

    @MockBean
    UserService userService;
}
