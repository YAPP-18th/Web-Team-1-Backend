package com.yapp18.retrospect.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    //스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야만 합니다**.
    ADMIN("ROLE_ADMIN", "관리자"),
    MEMBER("ROLE_MEMBER", "일반 회원"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;
}
