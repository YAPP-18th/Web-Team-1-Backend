package com.yapp18.retrospect.aop;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.CommentDto;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthAspect {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(com.yapp18.retrospect.aop.ExtractUser) && args(updateRequest, commentIdx)") // 메소드에 있는 인수 사용가능
    public Object extractUser(ProceedingJoinPoint pjp, CommentDto.UpdateRequest updateRequest, Long commentIdx) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));
        return pjp.proceed(new Object[] { user, updateRequest, commentIdx });
    }
}
