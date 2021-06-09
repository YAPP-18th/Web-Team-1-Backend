package com.yapp18.retrospect.security;

import com.yapp18.retrospect.config.TokenErrorInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 필요한 권한이 없이 접근하려 할때 403
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("{ "
                + "\"timestamp\" : \"" + new Timestamp(System.currentTimeMillis())
                + "\", \"error\" : { "
                + "\"code\" : \"" +  TokenErrorInfo.ACCESS_DENIED.getCode()
                + "\", \"exception\" : \"" +  TokenErrorInfo.ACCESS_DENIED.getException()
                + "\", \"message\" : \"" + TokenErrorInfo.ACCESS_DENIED.getMessage()
                + "\" }"
                + " }");
    }
}
