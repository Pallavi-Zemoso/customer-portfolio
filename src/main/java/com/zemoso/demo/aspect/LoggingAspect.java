package com.zemoso.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

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
        logger.info(" ====>   in @Before Advice: calling method: " + methodName);

        for(Object object : joinPoint.getArgs()){
            logger.info(" ====> Arguments: " + object);
        }
    }
}
