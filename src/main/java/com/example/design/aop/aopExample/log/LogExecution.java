package com.example.design.aop.aopExample.log;

import java.lang.annotation.*;

/**
 * 方法执行日志注解
 * 用于记录方法的执行时间、参数和返回值
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogExecution {
    
    /**
     * 日志级别，默认INFO
     */
    String level() default "INFO";
    
    /**
     * 是否记录方法参数，默认true
     */
    boolean logParams() default true;
    
    /**
     * 是否记录返回值，默认true
     */
    boolean logResult() default true;
    
    /**
     * 是否记录执行时间，默认true
     */
    boolean logTime() default true;
    
    /**
     * 自定义日志消息
     */
    String message() default "";
}