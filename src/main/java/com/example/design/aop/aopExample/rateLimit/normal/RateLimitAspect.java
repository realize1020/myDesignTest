package com.example.design.aop.aopExample.rateLimit.normal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 限流切面
 * 处理@RateLimit注解的方法
 */
@Aspect
@Component
public class RateLimitAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);
    
    // 限流器存储
    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final ExpressionParser parser = new SpelExpressionParser();
    private final Lock lock = new ReentrantLock();
    
    @Around("@annotation(com.example.design.aop.aopExample.rateLimit.normal.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
        if(rateLimitAnnotation == null){
            return joinPoint.proceed();
        }
        
        // 生成限流key
        String rateLimitKey = generateRateLimitKey(rateLimitAnnotation, joinPoint);
        
        // 获取或创建限流器
        RateLimiter rateLimiter = getOrCreateRateLimiter(rateLimitKey, rateLimitAnnotation);
        
        // 执行限流检查
        if (!rateLimiter.tryAcquire()) {
            handleRateLimitExceeded(rateLimitAnnotation, joinPoint, method);
        }
        
        // 执行目标方法
        return joinPoint.proceed();
    }
    
    /**
     * 生成限流key
     */
    private String generateRateLimitKey(RateLimit annotation, ProceedingJoinPoint joinPoint) {
        String key = annotation.key();
        
        if (key != null && !key.trim().isEmpty()) {
            // 使用SpEL表达式解析key
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setVariable("args", joinPoint.getArgs());
                context.setVariable("target", joinPoint.getTarget());
                context.setVariable("method", joinPoint.getSignature().getName());
                
                Expression expression = parser.parseExpression(key);
                Object result = expression.getValue(context);
                
                if (result != null) {
                    return result.toString();
                }
            } catch (Exception e) {
                logger.warn("解析限流key表达式失败: {}, 使用默认key", key, e);
            }
        }
        
        // 默认key：类名+方法名
        return joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
    }
    
    /**
     * 获取或创建限流器
     */
    private RateLimiter getOrCreateRateLimiter(String key, RateLimit annotation) {
        return rateLimiters.computeIfAbsent(key, k -> 
            new RateLimiter(annotation.limit(), annotation.window(), annotation.timeUnit()));
    }
    
    /**
     * 处理限流超限
     */
    private void handleRateLimitExceeded(RateLimit annotation, ProceedingJoinPoint joinPoint, Method method) {
        String methodInfo = joinPoint.getTarget().getClass().getSimpleName() + "." + method.getName();
        
        if (annotation.block()) {
            // 阻塞等待模式
            long maxWaitTime = annotation.maxWaitTime();
            if (maxWaitTime > 0) {
                logger.warn("限流阻塞等待: {} | 最大等待时间: {}ms", methodInfo, maxWaitTime);
                try {
                    Thread.sleep(maxWaitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("限流等待被中断", e);
                }
            }
        } else {
            // 直接抛出异常模式
            logger.warn("限流拒绝请求: {} | 错误消息: {}", methodInfo, annotation.message());
            throw new RateLimitException(annotation.message());
        }
    }
    
    /**
     * 简单的令牌桶限流器实现
     */
    private static class RateLimiter {
        private final int limit; // 时间窗口内的最大请求数
        private final long windowMillis; // 时间窗口大小（毫秒）
        private final AtomicInteger counter; // 当前窗口计数
        private volatile long windowStart; // 当前窗口开始时间
        
        public RateLimiter(int limit, int window, TimeUnit timeUnit) {
            this.limit = limit;
            this.windowMillis = timeUnit.toMillis(window);
            this.counter = new AtomicInteger(0);
            this.windowStart = System.currentTimeMillis();
        }
        
        public boolean tryAcquire() {
            long currentTime = System.currentTimeMillis();
            long currentWindowStart = windowStart;
            
            // 检查是否进入新的时间窗口
            if (currentTime - currentWindowStart >= windowMillis) {
                // 尝试重置窗口
                if (resetWindow(currentTime)) {
                    return true;
                }
            }
            
            // 在当前窗口内计数
            int currentCount = counter.incrementAndGet();
            
            // 如果计数超过限制，回滚并返回false
            if (currentCount > limit) {
                counter.decrementAndGet();
                return false;
            }
            
            return true;
        }
        
        private synchronized boolean resetWindow(long currentTime) {
            // 双重检查，防止并发重置
            if (currentTime - windowStart >= windowMillis) {
                windowStart = currentTime;
                counter.set(1); // 重置为1，因为当前请求需要计数
                return true;
            }
            return false;
        }
    }
    
    /**
     * 限流异常
     */
    public static class RateLimitException extends RuntimeException {
        public RateLimitException(String message) {
            super(message);
        }
        
        public RateLimitException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}