package com.yapp18.retrospect.security;

import com.yapp18.retrospect.config.ErrorInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 필요한 권한이 없이 접근하려 할때 403
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("{ \"message\" : \"" + ErrorInfo.ACCESS_DENIED.getMessage()
                + "\", \"code\" : \"" +  ErrorInfo.ACCESS_DENIED.getCode()
                + "\", \"status\" : " + ErrorInfo.ACCESS_DENIED.getStatus()
                + ", \"errors\" : [ ] }");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.sendRedirect("/login");
    }
}
