package com.yapp18.retrospect.auth.dto;

import com.yapp18.retrospect.auth.helper.SocialPlatform;
import com.yapp18.retrospect.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    //Session에는 인증된 사용자 정보만 필요합니다.
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}

