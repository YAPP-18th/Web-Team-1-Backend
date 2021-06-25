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
                        .email("ybell1028@daum.net")
                        .name("real-name")
                        .nickname("Walkman")
                        .profile("profile-url")
                        .provider(AuthProvider.kakao)
                        .providerId("12345")
                        .role(Role.MEMBER)
                        .job("job")
                        .intro("intro")
                        .build());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, "password", userPrincipal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
