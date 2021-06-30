package com.yapp18.retrospect.api.config;

import com.yapp18.retrospect.api.annotation.WithMockRetrospectUser;
import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.security.UserPrincipal;
import com.yapp18.retrospect.security.oauth2.AuthProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockRetrospectUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockRetrospectUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal userPrincipal =
                new UserPrincipal(User.builder()
                        .email("test@example.com")
                        .name("테스트이름")
                        .nickname("테스트닉네임")
                        .profile("profile-url")
                        .provider(AuthProvider.kakao)
                        .providerId("12345")
                        .role(Role.MEMBER)
                        .job("테스트직업")
                        .intro("테스트자기소개")
                        .build());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, "password", userPrincipal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
