package com.yapp18.retrospect.config;

import lombok.Getter;

@Getter
public enum ErrorInfo {
    EXPIRED_JWT(401, "TokenExpiredError", "만료된 JWT"),
    INVALID_SIGNATURE(401, "SignatureException", "유효하지 않은 JWT 서명"),
    MALFORMED_JWT(401, "MalformedJwtException", "올바르지 않은 JWT 구성"),
    UNSUPPORTED_JWT(401, "UnsupportedJwtException", "지원하지 않는 JWT 형식"),
    ILLEGAL_ARGUMENT(401, "IllegalArgumentException", "비어있는 Authortization 헤더"),
    ACCESS_DENIED(403, " AccessDenied", "접근이 거부됨");

    private final int status;
    private final String code;
    private final String message;

    ErrorInfo(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
