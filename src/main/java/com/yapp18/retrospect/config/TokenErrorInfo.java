package com.yapp18.retrospect.config;

import lombok.Getter;

@Getter
public enum TokenErrorInfo {
    EXPIRED_JWT("TE001", "TokenExpiredError", "만료된 JWT"),
    INVALID_SIGNATURE("TE002", "SignatureException", "유효하지 않은 JWT 서명"),
    MALFORMED_JWT( "TE003", "MalformedJwtException", "올바르지 않은 JWT 구성"),
    UNSUPPORTED_JWT( "TE004", "UnsupportedJwtException", "지원하지 않는 JWT 형식"),
    ILLEGAL_ARGUMENT( "TE005", "IllegalArgumentException", "비어있는 Authorization 헤더"),
    ILLEGAL_GRANTTYPE( "TE006", "IllegalArgumentException", "비어있는 GrantType"),
    ACCESS_DENIED( "TE007", "AccessDenied", "접근이 거부됨");

    private final String code;
    private final String exception;
    private final String message;

    TokenErrorInfo(String code, String exception, String message) {
        this.code = code;
        this.exception = exception;
        this.message = message;
    }
}
