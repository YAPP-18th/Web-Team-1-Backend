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
        private Long user_idx;
        private Role role;
        private String name;
        private String nickname;
        private String email;
        private String picture;
        private String platform;

        public Response(User entity) {
            this.user_idx = entity.getUser_idx();
            this.role = entity.getRole();
            this.name = entity.getName();
            this.nickname = entity.getNickname();
            this.email = entity.getEmail();
            this.picture = entity.getPicture();
            this.platform = entity.getPlatform();
        }
    }

    @Getter
    public static class totalResponse{
        private Long user_idx;
        private Role role;
        private String name;
        private String nickname;
        private String intro;
        private String email;
        private String picture;
        private String platform;
        private String job;

        public totalResponse(User entity) {
            this.user_idx = entity.getUser_idx();
            this.role = entity.getRole();
            this.name = entity.getName();
            this.nickname = entity.getNickname();
            this.intro = entity.getIntro();
            this.email = entity.getEmail();
            this.picture = entity.getPicture();
            this.platform = entity.getPlatform();
            this.job = entity.getJob();
        }
    }
}
