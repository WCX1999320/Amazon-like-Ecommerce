package com.cy.store.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimerAspect {
    @Around("execution(* com.cy.store.service.impl.*.*(..))")
    public Object Around(ProceedingJoinPoint pjp)throws Throwable{
        long start=System.currentTimeMillis();
        Object result=pjp.proceed();
        long end=System.currentTimeMillis();
        System.err.println("It takes:" + (end - start) + "ms.");
        return result;
    }
}
