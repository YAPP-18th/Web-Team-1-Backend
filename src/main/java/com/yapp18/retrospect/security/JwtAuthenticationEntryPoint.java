package com.yapp18.retrospect.security;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
        String exception = (String)request.getAttribute("exception");
        ErrorInfo errorInfo;

        logger.error(exception);
        if(exception.equals(ErrorInfo.ILLEGAL_ARGUMENT.getCode())){ // 헤더에 토큰이 없는 경우
            errorInfo = ErrorInfo.ILLEGAL_ARGUMENT;
            setResponse(response, errorInfo);
        } else if(exception.equals(ErrorInfo.EXPIRED_JWT.getCode())) {
            errorInfo = ErrorInfo.EXPIRED_JWT;
            setResponse(response, errorInfo);
        } else if(exception.equals(ErrorInfo.INVALID_SIGNATURE.getCode())) {
            errorInfo = ErrorInfo.INVALID_SIGNATURE;
            setResponse(response, errorInfo);
        } else if(exception.equals(ErrorInfo.MALFORMED_JWT.getCode())) {
            errorInfo = ErrorInfo.MALFORMED_JWT;
            setResponse(response, errorInfo);
        } else if(exception.equals(ErrorInfo.UNSUPPORTED_JWT.getCode())) {
            errorInfo = ErrorInfo.UNSUPPORTED_JWT;
            setResponse(response, errorInfo);
        }
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.sendRedirect("/login");
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ErrorInfo errorInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + errorInfo.getMessage()
                + "\", \"code\" : \"" +  errorInfo.getCode()
                + "\", \"status\" : " + errorInfo.getStatus()
                + " }");
    }
}
