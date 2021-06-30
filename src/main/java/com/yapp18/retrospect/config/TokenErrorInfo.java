package com.yapp18.retrospect.config;

import lombok.Getter;

@Getter
public enum TokenErrorInfo {
    EXPIRED_ACCESS("TE001", "TokenExpiredError", "Access Token이 만료됨"),
    EXPIRED_REFRESH("TE002", "TokenExpiredError", "Refresh Token이 만료됨"),
    INVALID_SIGNATURE_ACCESS("TE003", "SignatureException", "Access Token의 서명이 유효하지 않음"),
    INVALID_SIGNATURE_REFRESH("TE004", "SignatureException", "Refresh Token의 서명이 유효하지 않음"),
    MALFORMED_ACCESS( "TE005", "MalformedJwtException", "Access Token의 구성이 올바르지 않음"),
    MALFORMED_REFRESH( "TE006", "MalformedJwtException", "Refresh Token의 구성이 올바르지 않음"),
    UNSUPPORTED_ACCESS( "TE007", "UnsupportedJwtException", "지원하지 않는 형식의 Access Token"),
    UNSUPPORTED_REFRESH( "TE008", "UnsupportedJwtException", "지원하지 않는 형식의 Refresh Token"),
    ILLEGAL_ARGUMENT_ACCESS( "TE009", "IllegalArgumentException", "비어있는 Authorization 헤더"),
    ILLEGAL_ARGUMENT_REFRESH( "TE010", "IllegalArgumentException", "비어있는 refreshToken 바디"),
    ILLEGAL_GRANTTYPE( "TE011", "IllegalArgumentException", "비어있는 GrantType"),
    ACCESS_DENIED( "TE012", "AccessDenied", "접근이 거부됨"),
    NOT_EXPIRED_ACCESS( "TE013", "TokenNotExpired", "만료되지 않은 Access Token"),
    UNMATCHED_TOKEN_PAYLOAD( "TE014", "UnmatchedTokenPayload", "Access Token과 Refresh Token의 페이로드 불일치");

    private final String code;
    private final String exception;
    private final String message;

    TokenErrorInfo(String code, String exception, String message) {
        this.code = code;
        this.exception = exception;
        this.message = message;
    }
}
