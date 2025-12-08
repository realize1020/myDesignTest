package com.example.design.aop.simple;// LoggingAspect.java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(loggingAnnotation)")
    public void loggingPointcut(LoggingAnnotation loggingAnnotation) {
    }

    @Around("loggingPointcut(loggingAnnotation)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LoggingAnnotation loggingAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("开始执行方法: " + joinPoint.getSignature().getName());
        System.out.println("注解值: " + loggingAnnotation.value());

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            System.out.println("方法执行完成，耗时: " + (endTime - startTime) + "ms");
            return result;
        } catch (Exception e) {
            System.out.println("方法执行出错: " + e.getMessage());
            throw e;
        }
    }
}
