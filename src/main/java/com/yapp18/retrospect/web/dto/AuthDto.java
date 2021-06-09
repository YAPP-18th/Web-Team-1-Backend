package com.yapp18.retrospect.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AuthDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IssueResponse {
        @ApiModelProperty(value = "권한 타입 (Bearer)")
        private String grantType;
        @ApiModelProperty(value = "Access Token")
        private String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class ReissueRequest {
        @ApiModelProperty(value = "유효한 Refresh Token")
        private String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueResponse {
        @ApiModelProperty(value = "권한 타입 (Bearer)")
        private String grantType;
        @ApiModelProperty(value = "재발급 된 Access Token")
        private String accessToken;
    }
}
