package com.example.design.miniSpring.ioc;

/**
 * 定义bean销毁时的行为
 */
public interface DisposableBean {
    /**
     * 销毁时调用
     */
    void destroy() throws Exception;
} 