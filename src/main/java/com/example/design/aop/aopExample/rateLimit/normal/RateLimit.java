package com.example.design.aop.aopExample.rateLimit.normal;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 方法限流注解
 * 用于限制方法的调用频率
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    
    /**
     * 限流key，支持SpEL表达式
     */
    String key() default "";
    
    /**
     * 时间窗口内的最大请求数
     */
    int limit() default 100;
    
    /**
     * 时间窗口大小
     */
    int window() default 60;
    
    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    /**
     * 限流时的错误消息
     */
    String message() default "请求过于频繁，请稍后再试";
    
    /**
     * 是否阻塞等待，默认false（直接抛出异常）
     */
    boolean block() default false;
    
    /**
     * 阻塞等待的最大时间（毫秒）
     */
    long maxWaitTime() default 0;
}