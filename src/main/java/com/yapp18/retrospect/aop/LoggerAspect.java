package com.yapp18.retrospect.aop;

import com.google.common.base.Stopwatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class LoggerAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.yapp18.retrospect.web.controller.*Controller.*(..)) || execution(* com.yapp18.retrospect.service.*Service.*(..))")
    public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();
        if (name.contains("Controller")) {
            type = "Controller (Amount Time) ===> ";
        } else if (name.contains("Service")) {
            type = "Service ===> ";
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object proceed = joinPoint.proceed(); // target method 실행
        stopwatch.stop();
        logger.info(type + name + "." + joinPoint.getSignature().getName() + "() : " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        return proceed;
    }

    @Before("execution(* com.yapp18.retrospect.web.controller.*Controller.*(..))")
    public void LoggingRequest(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        System.out.println("Requested HTTP Method :: " + request.getMethod());
        System.out.println("Requested URI :: " + request.getRequestURI());
    }
}
