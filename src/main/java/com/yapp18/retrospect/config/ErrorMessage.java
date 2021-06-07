package com.yapp18.retrospect.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    USER_NULL("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    POST_NULL("존재하지 않는 게시물입니다.", HttpStatus.NOT_FOUND),
    COMMENT_NULL("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND);

    private final String errorMessage;
    private final HttpStatus statusCode;

    ErrorMessage(String errorMessage, HttpStatus statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}
