package com.yapp18.retrospect.config;

import lombok.Getter;

@Getter
public enum ErrorInfo {
    USER_NULL("EE001","존재하지 않는 사용자입니다."),
    POST_NULL("EE002", "존재하지 않는 회고글입니다."),
    COMMENT_NULL("EE003", "존재하지 않는 댓글입니다."),
    TEMPLATE_NULL("EE004", "존재하지 않는 템플릿입니다."),
    TAG_NULL("EE005", "존재하지 않는 태그입니다."),
    IMAGE_NULL("EE006", "존재하지 않는 이미지입니다."),
    LIKE_NULL("EE007", "존재하지 않는 스크랩입니다.");

    private final String errorCode;
    private final String errorMessage;

    ErrorInfo(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
