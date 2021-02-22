package com.zemoso.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.zemoso.demo.controller.*.*(..))")
    private void forControllerPackage(){}

    @Pointcut("execution(* com.zemoso.demo.service.*.*(..))")
    private void forServicePackage(){}

    @Pointcut("execution(* com.zemoso.demo.dao.*.*(..))")
    private void forDAOPackage(){}

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.*(..))")
    public void forJPARepository() {}

    @Pointcut("execution(* com.zemoso.demo.exception.*.*(..))")
    private void forExceptionPackage(){}

    @Pointcut("execution(* com.zemoso.demo.entity.*.*(..))")
    private void forEntityPackages(){}

    @Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage() || forJPARepository()")
    private void forAppFlow(){}

    @Pointcut("forAppFlow() || forEntityPackages() || forExceptionPackage()")
    private void forAllPackages(){}

    @Before("forAppFlow()")
    public void logBeforeAdvice(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().toShortString();
        log.info(" ====>   in @Before Advice: calling method: " + methodName);

        for(Object object : joinPoint.getArgs()){
            log.info(" ====> Arguments: " + object);
        }
    }
}
