package com.yapp18.retrospect.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Pointcut("execution(* com.yapp18.retrospect.web.controller.*Controller.*(..)) || execution(* com.yapp18.retrospect.service.*Service.*(..))")
//    public void allPointcut() {
//    }
//
//    @Around("allPointcut()")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        String type = "";
//        String name = joinPoint.getSignature().getDeclaringTypeName();
//        if (name.contains("Controller")) {
//            type = "Controller ===> ";
//        } else if (name.contains("Service")) {
//            type = "Service ===> ";
//        }
//        logger.info(type + name + "." + joinPoint.getSignature().getName() + "()");
//        return joinPoint.proceed();
//    }
}
