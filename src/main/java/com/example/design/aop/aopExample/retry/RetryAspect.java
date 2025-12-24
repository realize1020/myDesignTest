package com.example.design.aop.aopExample.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 重试切面
 * 处理@Retryable注解的方法
 */
@Aspect
@Component
public class RetryAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(RetryAspect.class);
    
    @Around("@annotation(com.example.design.aop.aopExample.log.LogExecution)")
    public Object retryOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Retryable retryAnnotation = method.getAnnotation(Retryable.class);
        if(retryAnnotation == null){
            return joinPoint.proceed();
        }
        
        int maxAttempts = retryAnnotation.maxAttempts();
        long delay = retryAnnotation.delay();
        boolean exponentialBackoff = retryAnnotation.exponentialBackoff();
        long maxDelay = retryAnnotation.maxDelay();
        
        Set<Class<? extends Throwable>> retryFor = new HashSet<>(Arrays.asList(retryAnnotation.retryFor()));
        Set<Class<? extends Throwable>> noRetryFor = new HashSet<>(Arrays.asList(retryAnnotation.noRetryFor()));
        
        int attempt = 0;
        Throwable lastException;
        
        do {
            attempt++;
            try {
                logger.debug("执行方法重试，第{}次尝试: {}.{}", 
                           attempt, joinPoint.getTarget().getClass().getSimpleName(), method.getName());
                
                return joinPoint.proceed();
                
            } catch (Throwable throwable) {
                lastException = throwable;
                
                // 检查是否需要重试
                if (!shouldRetry(throwable, retryFor, noRetryFor) || attempt >= maxAttempts) {
                    logger.warn("方法执行失败，不再重试: {}.{} | 异常: {} | 尝试次数: {}",
                              joinPoint.getTarget().getClass().getSimpleName(), method.getName(),
                              throwable.getClass().getSimpleName(), attempt);
                    throw throwable;
                }
                
                logger.warn("方法执行失败，准备重试: {}.{} | 异常: {} | 第{}次重试",
                          joinPoint.getTarget().getClass().getSimpleName(), method.getName(),
                          throwable.getClass().getSimpleName(), attempt);
                
                // 计算等待时间
                long waitTime = calculateWaitTime(delay, exponentialBackoff, attempt, maxDelay);
                if (waitTime > 0) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("重试被中断", e);
                    }
                }
            }
        } while (attempt < maxAttempts);
        
        throw lastException;
    }
    
    /**
     * 判断是否需要重试
     */
    private boolean shouldRetry(Throwable throwable, 
                               Set<Class<? extends Throwable>> retryFor,
                               Set<Class<? extends Throwable>> noRetryFor) {
        // 检查是否在不重试的异常列表中
        for (Class<? extends Throwable> noRetryClass : noRetryFor) {
            if (noRetryClass.isInstance(throwable)) {
                return false;
            }
        }
        
        // 检查是否在重试的异常列表中
        for (Class<? extends Throwable> retryClass : retryFor) {
            if (retryClass.isInstance(throwable)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 计算等待时间
     */
    private long calculateWaitTime(long baseDelay, boolean exponential, int attempt, long maxDelay) {
        if (!exponential) {
            return baseDelay;
        }
        
        long waitTime = baseDelay * (long) Math.pow(2, attempt - 1);
        return Math.min(waitTime, maxDelay);
    }
}