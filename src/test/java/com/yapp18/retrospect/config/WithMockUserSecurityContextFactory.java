package com.yapp18.retrospect.config;

import com.yapp18.retrospect.annotation.WithMockRetrospectUser;
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
        User user = User.builder()
                .userIdx(1L)
                .email("user1@example.com")
                .name("이름1")
                .nickname("닉네임1")
                .profile("프로필1")
                .job("직업1")
                .intro("자기소개1")
                .provider(AuthProvider.kakao)
                .providerId("1")
                .role(Role.MEMBER)
                .build();
//        user.setCreatedAt(LocalDateTime.of(2021, 5, 23, 11, 42, 5));
//        user.setModifiedAt(LocalDateTime.of(2021, 5, 23, 11, 42, 5));

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, "password", userPrincipal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
