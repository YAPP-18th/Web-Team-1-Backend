package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ErrorMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityNullException extends NullPointerException {
    private String code;
    private String message;


    public EntityNullException(ErrorMessage errorMessage) {
        this.code = errorMessage.getErrorCode();
        this.message = errorMessage.getErrorMessage();
    }

    public EntityNullException(String s, ErrorMessage errorMessage) {
        super(s);
        this.code = errorMessage.getErrorCode();
        this.message = errorMessage.getErrorMessage();
    }
}
