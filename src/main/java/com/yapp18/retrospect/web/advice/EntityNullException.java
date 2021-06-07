package com.yapp18.retrospect.web.advice;

import com.yapp18.retrospect.config.ErrorMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityNullException extends NullPointerException {
    private ErrorMessage errorMessage;
    private String message;
    private HttpStatus status;

    public EntityNullException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        this.message = errorMessage.getErrorMessage();
        this.status = errorMessage.getStatusCode();
    }

    public EntityNullException(String s, ErrorMessage errorMessage) {
        super(s);
        this.errorMessage = errorMessage;
        this.message = errorMessage.getErrorMessage();
        this.status = errorMessage.getStatusCode();
    }
}
