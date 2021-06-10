package com.yapp18.retrospect.security;

import com.yapp18.retrospect.config.TokenErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
        String errorCode = (String)request.getAttribute("errorCode");

        logger.error(errorCode);
        if(errorCode.equals(TokenErrorInfo.ILLEGAL_ARGUMENT.getCode())){ // 헤더에 토큰이 없는 경우
            setResponse(response, TokenErrorInfo.ILLEGAL_ARGUMENT);
        } else if(errorCode.equals(TokenErrorInfo.ILLEGAL_GRANTTYPE.getCode())){
            setResponse(response, TokenErrorInfo.ILLEGAL_GRANTTYPE);
        } else if(errorCode.equals(TokenErrorInfo.EXPIRED_JWT.getCode())) {
            setResponse(response, TokenErrorInfo.EXPIRED_JWT);
        } else if(errorCode.equals(TokenErrorInfo.INVALID_SIGNATURE.getCode())) {
            setResponse(response, TokenErrorInfo.INVALID_SIGNATURE);
        } else if(errorCode.equals(TokenErrorInfo.MALFORMED_JWT.getCode())) {
            setResponse(response, TokenErrorInfo.MALFORMED_JWT);
        } else if(errorCode.equals(TokenErrorInfo.UNSUPPORTED_JWT.getCode())) {
            setResponse(response, TokenErrorInfo.UNSUPPORTED_JWT);
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, TokenErrorInfo tokenErrorInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ "
                + "\"timestamp\" : \"" + new Timestamp(System.currentTimeMillis())
                + "\", \"error\" : { "
                    + "\"code\" : \"" +  tokenErrorInfo.getCode()
                    + "\", \"exception\" : \"" +  tokenErrorInfo.getException()
                    + "\", \"message\" : \"" + tokenErrorInfo.getMessage()
                    + "\" }"
                + " }");
    }
}
