package com.example.design.aop.aopExample.rateLimit.advance;

import com.example.design.aop.aopExample.rateLimit.normal.RateLimit;
import com.example.design.aop.aopExample.rateLimit.normal.RateLimitConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 高级限流切面
 * 支持多种限流算法和统计功能
 * ## 核心功能
 * 1. 多种限流算法支持 ：
 *
 *    - 固定窗口限流器 ：基于时间窗口的简单计数
 *    - 滑动窗口限流器 ：更精确的时间窗口控制
 *    - 令牌桶限流器 ：支持突发流量和匀速处理
 *    - 漏桶限流器 ：严格限制流出速率
 * 2. 灵活的限流策略 ：
 *
 *    - 支持阻塞等待或直接抛出异常
 *    - 可配置最大等待时间
 *    - SpEL表达式支持动态限流key
 * 3. 完整的统计功能 ：
 *
 *    - 请求总数、成功数、拒绝数统计
 *    - 错误率和平均响应时间计算
 *    - 等待请求统计
 */
@Aspect
@Component
public class AdvancedRateLimitAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedRateLimitAspect.class);
    
    @Autowired(required = false)
    private RateLimitConfig rateLimitConfig;
    
    // 限流器存储
    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final ExpressionParser parser = new SpelExpressionParser();
    private final Lock lock = new ReentrantLock();
    
    // 统计信息
    private final ConcurrentHashMap<String, RateLimitStatistics> statistics = new ConcurrentHashMap<>();
    
    @Around("@annotation(com.example.design.aop.aopExample.rateLimit.normal.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        // 检查是否启用限流
        if (rateLimitConfig != null && !rateLimitConfig.isEnabled()) {
            return joinPoint.proceed();
        }
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
        if(rateLimitAnnotation == null){
            return joinPoint.proceed();
        }
        
        // 生成限流key
        String rateLimitKey = generateRateLimitKey(rateLimitAnnotation, joinPoint);
        
        // 获取统计信息
        RateLimitStatistics stats = statistics.computeIfAbsent(rateLimitKey, 
            k -> new RateLimitStatistics());
        
        // 获取或创建限流器
        RateLimiter rateLimiter = getOrCreateRateLimiter(rateLimitKey, rateLimitAnnotation);
        
        // 记录请求
        stats.recordRequest();
        
        // 执行限流检查
        long startTime = System.currentTimeMillis();
        boolean acquired = false;
        
        try {
            acquired = rateLimiter.tryAcquire();
            
            if (!acquired) {
                stats.recordRejection();
                handleRateLimitExceeded(rateLimitAnnotation, joinPoint, method, stats);
            }
            
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录成功
            stats.recordSuccess(System.currentTimeMillis() - startTime);
            
            return result;
            
        } catch (Throwable throwable) {
            stats.recordError();
            throw throwable;
        }
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
        return rateLimiters.computeIfAbsent(key, k -> {
            String algorithm = getAlgorithm(annotation);
            switch (algorithm) {
                case "fixed_window":
                    return new FixedWindowRateLimiter(annotation.limit(), annotation.window(), annotation.timeUnit());
                case "sliding_window":
                    return new SlidingWindowRateLimiter(annotation.limit(), annotation.window(), annotation.timeUnit());
                case "leaky_bucket":
                    return new LeakyBucketRateLimiter(annotation.limit(), annotation.window(), annotation.timeUnit());
                default:
                    return new TokenBucketRateLimiter(annotation.limit(), annotation.window(), annotation.timeUnit());
            }
        });
    }
    
    /**
     * 获取限流算法
     */
    private String getAlgorithm(RateLimit annotation) {
        // 这里可以根据配置或注解属性选择算法
        // 简化实现，返回固定算法
        return "token_bucket";
    }
    
    /**
     * 处理限流超限
     */
    private void handleRateLimitExceeded(RateLimit annotation, ProceedingJoinPoint joinPoint, 
                                       Method method, RateLimitStatistics stats) {
        String methodInfo = joinPoint.getTarget().getClass().getSimpleName() + "." + method.getName();
        
        if (annotation.block()) {
            // 阻塞等待模式
            long maxWaitTime = annotation.maxWaitTime();
            if (maxWaitTime > 0) {
                logger.warn("限流阻塞等待: {} | 最大等待时间: {}ms", methodInfo, maxWaitTime);
                try {
                    Thread.sleep(maxWaitTime);
                    stats.recordWait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RateLimitException("限流等待被中断", e);
                }
            }
        } else {
            // 直接抛出异常模式
            logger.warn("限流拒绝请求: {} | 错误消息: {}", methodInfo, annotation.message());
            throw new RateLimitException(annotation.message());
        }
    }
    
    /**
     * 限流器接口
     */
    private interface RateLimiter {
        boolean tryAcquire();
    }
    
    /**
     * 固定窗口限流器
     */
    private static class FixedWindowRateLimiter implements RateLimiter {
        private final int limit;
        private final long windowMillis;
        private final AtomicLong counter;
        private volatile long windowStart;
        
        public FixedWindowRateLimiter(int limit, int window, TimeUnit timeUnit) {
            this.limit = limit;
            this.windowMillis = timeUnit.toMillis(window);
            this.counter = new AtomicLong(0);
            this.windowStart = System.currentTimeMillis();
        }
        
        @Override
        public boolean tryAcquire() {
            long currentTime = System.currentTimeMillis();
            
            // 检查是否进入新的时间窗口
            if (currentTime - windowStart >= windowMillis) {
                synchronized (this) {
                    if (currentTime - windowStart >= windowMillis) {
                        windowStart = currentTime;
                        counter.set(0);
                    }
                }
            }
            
            // 检查是否超过限制
            return counter.incrementAndGet() <= limit;
        }
    }
    
    /**
     * 滑动窗口限流器
     */
    private static class SlidingWindowRateLimiter implements RateLimiter {
        private final int limit;
        private final long windowMillis;
        private final ConcurrentHashMap<Long, AtomicLong> windows;
        private final Lock lock = new ReentrantLock();
        
        public SlidingWindowRateLimiter(int limit, int window, TimeUnit timeUnit) {
            this.limit = limit;
            this.windowMillis = timeUnit.toMillis(window);
            this.windows = new ConcurrentHashMap<>();
        }
        
        @Override
        public boolean tryAcquire() {
            long currentTime = System.currentTimeMillis();
            long currentWindow = currentTime / 1000; // 每秒一个窗口
            
            // 清理过期窗口
            cleanExpiredWindows(currentTime);
            
            // 获取当前窗口计数
            AtomicLong currentCount = windows.computeIfAbsent(currentWindow, k -> new AtomicLong(0));
            
            // 计算滑动窗口内的总请求数
            long totalRequests = calculateTotalRequests(currentTime);
            
            // 检查是否超过限制
            if (totalRequests < limit) {
                currentCount.incrementAndGet();
                return true;
            }
            
            return false;
        }
        
        private void cleanExpiredWindows(long currentTime) {
            long expiredTime = currentTime - windowMillis;
            long expiredWindow = expiredTime / 1000;
            
            windows.keySet().removeIf(window -> window <= expiredWindow);
        }
        
        private long calculateTotalRequests(long currentTime) {
            long startTime = currentTime - windowMillis;
            long startWindow = startTime / 1000;
            
            return windows.entrySet().stream()
                .filter(entry -> entry.getKey() >= startWindow)
                .mapToLong(entry -> entry.getValue().get())
                .sum();
        }
    }
    
    /**
     * 令牌桶限流器
     */
    private static class TokenBucketRateLimiter implements RateLimiter {
        private final int capacity; // 桶容量
        private final double refillRate; // 每秒补充的令牌数
        private double tokens; // 当前令牌数
        private long lastRefillTime; // 上次补充时间
        
        public TokenBucketRateLimiter(int limit, int window, TimeUnit timeUnit) {
            this.capacity = limit;
            this.refillRate = (double) limit / timeUnit.toSeconds(window);
            this.tokens = limit;
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        @Override
        public synchronized boolean tryAcquire() {
            refillTokens();
            
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            
            return false;
        }
        
        private void refillTokens() {
            long currentTime = System.currentTimeMillis();
            double secondsSinceLastRefill = (currentTime - lastRefillTime) / 1000.0;
            
            tokens = Math.min(capacity, tokens + secondsSinceLastRefill * refillRate);
            lastRefillTime = currentTime;
        }
    }
    
    /**
     * 漏桶限流器
     */
    private static class LeakyBucketRateLimiter implements RateLimiter {
        private final int capacity; // 桶容量
        private final long leakInterval; // 漏水间隔（毫秒）
        private int waterLevel; // 当前水位
        private long lastLeakTime; // 上次漏水时间
        
        public LeakyBucketRateLimiter(int limit, int window, TimeUnit timeUnit) {
            this.capacity = limit;
            this.leakInterval = timeUnit.toMillis(window) / limit;
            this.waterLevel = 0;
            this.lastLeakTime = System.currentTimeMillis();
        }
        
        @Override
        public synchronized boolean tryAcquire() {
            leakWater();
            
            if (waterLevel < capacity) {
                waterLevel++;
                return true;
            }
            
            return false;
        }
        
        private void leakWater() {
            long currentTime = System.currentTimeMillis();
            long leaks = (currentTime - lastLeakTime) / leakInterval;
            
            if (leaks > 0) {
                waterLevel = Math.max(0, waterLevel - (int) leaks);
                lastLeakTime = currentTime;
            }
        }
    }
    
    /**
     * 限流统计信息
     */
    private static class RateLimitStatistics {
        private final AtomicLong totalRequests = new AtomicLong(0);
        private final AtomicLong successfulRequests = new AtomicLong(0);
        private final AtomicLong rejectedRequests = new AtomicLong(0);
        private final AtomicLong errorRequests = new AtomicLong(0);
        private final AtomicLong waitingRequests = new AtomicLong(0);
        private final AtomicLong totalResponseTime = new AtomicLong(0);
        
        public void recordRequest() {
            totalRequests.incrementAndGet();
        }
        
        public void recordSuccess(long responseTime) {
            successfulRequests.incrementAndGet();
            totalResponseTime.addAndGet(responseTime);
        }
        
        public void recordRejection() {
            rejectedRequests.incrementAndGet();
        }
        
        public void recordError() {
            errorRequests.incrementAndGet();
        }
        
        public void recordWait() {
            waitingRequests.incrementAndGet();
        }
        
        public double getSuccessRate() {
            long total = totalRequests.get();
            return total > 0 ? (double) successfulRequests.get() / total : 0.0;
        }
        
        public double getAverageResponseTime() {
            long success = successfulRequests.get();
            return success > 0 ? (double) totalResponseTime.get() / success : 0.0;
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