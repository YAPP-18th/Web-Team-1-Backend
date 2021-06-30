package com.yapp18.retrospect.web.dto;

import com.yapp18.retrospect.domain.user.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto implements Serializable {
    @Getter
    @NoArgsConstructor
    @ApiModel(value = "사용자 프로필 수정", description = "사용자 프로필 수정 요청 모델")
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

    @Getter
    @ApiModel(value = "사용자 프로필 조회", description = "사용자 프로필 응답 모델")
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
        @ApiModelProperty(value = "본인 프로필 여부")
        private boolean mine;

        @Builder
        public ProfileResponse(String name, String nickname, String profile, String job, String intro, boolean mine) {
            this.name = name;
            this.nickname = nickname;
            this.profile = profile;
            this.job = job;
            this.intro = intro;
            this.mine = mine;
        }
    }
}
