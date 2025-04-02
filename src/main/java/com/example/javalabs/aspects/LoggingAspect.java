package com.example.javalabs.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning(pointcut = "execution(* com.example.javalabs.controllers.FreelancersController.*(..))",
                    returning = "result")
    public void logSuccessfulExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Method {} executed successfully with args: {}, result: {}", methodName, args, result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.javalabs.controllers.FreelancersController.*(..))",
                   throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.error("Method {} failed with args: {}, exception: {}", methodName, args, ex.getMessage(), ex);
    }
}