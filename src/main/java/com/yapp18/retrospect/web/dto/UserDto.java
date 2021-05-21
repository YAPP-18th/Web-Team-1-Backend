package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.domain.user.User;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserDto {
    @Getter
    public static class Response{
        private Long userIdx;
        private Role role;
        private String name;
        private String nickname;
        private String email;
        private String picture;
        private String platform;

        public Response(User entity) {
            this.userIdx = entity.getUserIdx();
            this.role = entity.getRole();
            this.name = entity.getName();
            this.nickname = entity.getNickname();
            this.email = entity.getEmail();
            this.picture = entity.getProfile();
            this.platform = entity.getProvider();
        }
    }

    @Getter
    public static class totalResponse{
        private Long userIdx;
        private Role role;
        private String name;
        private String nickname;
        private String intro;
        private String email;
        private String picture;
        private String provider;
        private String job;

        public totalResponse(User entity) {
            this.userIdx = entity.getUserIdx();
            this.role = entity.getRole();
            this.name = entity.getName();
            this.nickname = entity.getNickname();
            this.intro = entity.getIntro();
            this.email = entity.getEmail();
            this.picture = entity.getProfile();
            this.provider = entity.getProvider();
            this.job = entity.getJob();
        }
    }
}
