package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ErrorInfo;
import lombok.Getter;

@Getter
public class EntityNullException extends NullPointerException {
    private String code;
    private String message;


    public EntityNullException(ErrorInfo errorInfo) {
        this.code = errorInfo.getErrorCode();
        this.message = errorInfo.getErrorMessage();
    }

    public EntityNullException(String s, ErrorInfo errorInfo) {
        super(s);
        this.code = errorInfo.getErrorCode();
        this.message = errorInfo.getErrorMessage();
    }
}
