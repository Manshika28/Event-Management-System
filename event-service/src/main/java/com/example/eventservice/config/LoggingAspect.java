package com.example.eventservice.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.example.eventservice.controller.*.*(..))")
    public Object log(ProceedingJoinPoint p) throws Throwable {
        System.out.println("[AOP] Entering: " + p.getSignature());
        Object o = p.proceed();
        System.out.println("[AOP] Exiting: " + p.getSignature());
        return o;
    }
}