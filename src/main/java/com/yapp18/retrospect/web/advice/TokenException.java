package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import lombok.Getter;

@Getter
public class TokenException extends IllegalArgumentException {
    private String code;
    private String exception;
    private String message;

    public TokenException(TokenErrorInfo tokenErrorInfo) {
        this.code = tokenErrorInfo.getCode();
        this.exception = tokenErrorInfo.getException();
        this.message = tokenErrorInfo.getMessage();
    }
}