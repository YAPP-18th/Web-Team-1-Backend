package com.yapp18.retrospect.config;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    // common
    BAD_REQUEST(400,"Bad request."),
    METHOD_NOT_ALLOWED(405, "Method not allowed."),
    BAD_GATEWAY(502,"Bad GateWay"),
    INTERNAL_SERVER_ERROR(500,"INTERNAL_SERVER_ERROR");


//    private final String code;
    private final String message;
    private final int status;


    ErrorMessage( int status ,String message) {
        this.status = status;
//        this.code = code;
        this.message = message;
    }
}
