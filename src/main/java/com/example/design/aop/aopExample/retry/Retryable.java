package com.example.design.aop.aopExample.retry;

import java.lang.annotation.*;

/**
 * 方法重试注解
 * 用于在方法执行失败时自动重试
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retryable {
    
    /**
     * 最大重试次数，默认3次
     */
    int maxAttempts() default 3;
    
    /**
     * 重试间隔（毫秒），默认1000ms
     */
    long delay() default 1000;
    
    /**
     * 需要重试的异常类型
     */
    Class<? extends Throwable>[] retryFor() default {Exception.class};
    
    /**
     * 不需要重试的异常类型
     */
    Class<? extends Throwable>[] noRetryFor() default {};
    
    /**
     * 是否启用指数退避，默认false
     */
    boolean exponentialBackoff() default false;
    
    /**
     * 最大重试间隔（毫秒），默认5000ms
     */
    long maxDelay() default 5000;
}