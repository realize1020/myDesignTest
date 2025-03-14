package com.example.design.miniSpring.ioc;


/**
 * 定义一个对象工厂接口，用于创建bean实例
 *
 * @author kama
 * @version 1.0.0
 */
@FunctionalInterface
public interface ObjectFactory<T> {
    /**
     * 获取对象实例
     *
     * @return 对象实例
     * @throws RuntimeException 如果获取对象失败
     */
    T getObject() throws RuntimeException;
} 