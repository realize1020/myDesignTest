package com.example.design.aop.aopExample.log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 日志切面
 * 处理@LogExecution注解的方法
 */
@Aspect
@Component
public class LogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    /**
     * 环绕通知：记录方法执行日志
     */
    @Around("@annotation(com.example.design.aop.aopExample.log.LogExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogExecution logAnnotation = method.getAnnotation(LogExecution.class);
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;
        
        try {
            // 记录方法开始
            logMethodStart(logAnnotation, className, methodName, joinPoint.getArgs());
            
            // 执行目标方法
            result = joinPoint.proceed();
            
            return result;
            
        } catch (Throwable throwable) {
            exception = throwable;
            throw throwable;
            
        } finally {
            // 记录方法结束
            long executionTime = System.currentTimeMillis() - startTime;
            logMethodEnd(logAnnotation, className, methodName, result, exception, executionTime);
        }
    }
    
    /**
     * 记录方法开始
     */
    private void logMethodStart(LogExecution annotation, String className, String methodName, Object[] args) {
        if (!shouldLog(annotation.level())) {
            return;
        }
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("开始执行方法: ").append(className).append(".").append(methodName);
        
        if (annotation.logParams() && args != null && args.length > 0) {
            logMessage.append(" | 参数: ").append(Arrays.toString(args));
        }
        
        if (!annotation.message().isEmpty()) {
            logMessage.append(" | ").append(annotation.message());
        }
        
        logByLevel(annotation.level(), logMessage.toString());
    }
    
    /**
     * 记录方法结束
     */
    private void logMethodEnd(LogExecution annotation, String className, String methodName, 
                            Object result, Throwable exception, long executionTime) {
        if (!shouldLog(annotation.level())) {
            return;
        }
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("方法执行完成: ").append(className).append(".").append(methodName);
        
        if (annotation.logTime()) {
            logMessage.append(" | 耗时: ").append(executionTime).append("ms");
        }
        
        if (exception != null) {
            logMessage.append(" | 异常: ").append(exception.getClass().getSimpleName())
                     .append(": ").append(exception.getMessage());
        } else if (annotation.logResult() && result != null) {
            logMessage.append(" | 结果: ").append(truncateResult(result.toString()));
        }
        
        if (!annotation.message().isEmpty()) {
            logMessage.append(" | ").append(annotation.message());
        }
        
        logByLevel(annotation.level(), logMessage.toString());
    }
    
    /**
     * 根据日志级别判断是否需要记录
     */
    private boolean shouldLog(String level) {
        // 简化实现，实际项目中可以根据配置动态调整
        return true;
    }
    
    /**
     * 根据级别记录日志
     */
    private void logByLevel(String level, String message) {
        switch (level.toUpperCase()) {
            case "DEBUG":
                logger.debug(message);
                break;
            case "WARN":
                logger.warn(message);
                break;
            case "ERROR":
                logger.error(message);
                break;
            default:
                logger.info(message);
        }
    }
    
    /**
     * 截断过长的结果字符串
     */
    private String truncateResult(String result) {
        if (result.length() > 100) {
            return result.substring(0, 100) + "...";
        }
        return result;
    }
}