package com.autotests.lichessbackend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.autotests.lichessbackend.service.*.*(..))")
    public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.trace("Entering service method: {}.{}", className, methodName);
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.debug("Service method completed: {}.{} in {} ms",
                    className,
                    methodName,
                    duration);
            return result;
        } catch (Throwable exception) {
            log.error("Exception in service method: {}.{}",
                    className,
                    methodName,
                    exception);
            throw exception;
        }
    }
}