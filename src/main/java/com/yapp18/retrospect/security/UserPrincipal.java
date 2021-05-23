package com.yapp18.retrospect.security;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;



@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    // 이 클래스는 User를 생성자로 전달받아 Spring Security에 User 정보를 전달한다.
    // UserPrincipal 클래스는 인증된 Spring Security 주체를 나타낸다. (토큰을 통해)
    // 인증된 사용자의 세부 정보를 포함한다.
    private String providerId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    // 여기까지 OAuth2User, UserDetails 구현체 필수, 아래는 직접 추가한것
    private Long userIdx;
    private String nickname;

    @Builder
    public UserPrincipal(String providerId, String email, String password, Collection<? extends GrantedAuthority> authorities, Long userIdx, String nickname) {
        this.providerId = providerId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.userIdx = userIdx;
        this.nickname = nickname;
    }


    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRoleKey()));

        return UserPrincipal.builder()
                .providerId(user.getProviderId())
                .email(user.getEmail())
                .password(null)
                .authorities(authorities)
                .userIdx(user.getUserIdx())
                .nickname(user.getNickname())
                .build();
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(providerId);
    }
}
