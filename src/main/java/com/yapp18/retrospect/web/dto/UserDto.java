package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.domain.user.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto implements Serializable {
//    @ApiModelProperty(value = "회원 번호")
//    private Long userIdx;
//    @ApiModelProperty(value = "회원 권한")
//    private Role role;
//    @ApiModelProperty(value = "회원 이름")
//    private String name;
//    @ApiModelProperty(value = "회원 닉네임")
//    private String nickname;
//    @ApiModelProperty(value = "회원 이메일")
//    private String email;
//    @ApiModelProperty(value = "회원 사진 URL")
//    private String profile;
//    @ApiModelProperty(value = "회원 가입 플랫폼")
//    private String provider;
//    @ApiModelProperty(value = "회원 가입 플랫폼 ID")
//    private String providerId;
//    @ApiModelProperty(value = "회원 직업")
//    private String job;
//    @ApiModelProperty(value = "회원 자기소개")
//    private String intro;

    @Getter
    public static class ProfileResponse {
        @ApiModelProperty(value = "회원 이름")
        private String name;
        @ApiModelProperty(value = "회원 닉네임")
        private String nickname;
        @ApiModelProperty(value = "회원 사진 URL")
        private String profile;
        @ApiModelProperty(value = "회원 직업")
        private String job;
        @ApiModelProperty(value = "회원 자기소개")
        private String intro;

        @Builder
        public ProfileResponse(String name, String nickname, String profile, String job, String intro) {
            this.name = name;
            this.nickname = nickname;
            this.profile = profile;
            this.job = job;
            this.intro = intro;
        }
    }

    @Getter
    public static class UpdateRequest {
        @ApiModelProperty(value = "회원 이름")
        private String name;
        @ApiModelProperty(value = "회원 닉네임")
        private String nickname;
        @ApiModelProperty(value = "회원 사진 URL")
        private String profile;
        @ApiModelProperty(value = "회원 직업")
        private String job;
        @ApiModelProperty(value = "회원 자기소개")
        private String intro;

        @Builder
        public UpdateRequest(String name, String nickname, String profile, String job, String intro) {
            this.name = name;
            this.nickname = nickname;
            this.profile = profile;
            this.job = job;
            this.intro = intro;
        }
    }
}
