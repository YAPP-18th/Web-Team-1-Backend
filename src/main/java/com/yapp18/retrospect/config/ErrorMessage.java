package com.yapp18.retrospect.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    USER_NULL("E001","존재하지 않는 사용자입니다."),
    POST_NULL("E002", "존재하지 않는 게시물입니다."),
    COMMENT_NULL("E003", "존재하지 않는 댓글입니다.");

    private final String errorCode;
    private final String errorMessage;

    ErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
