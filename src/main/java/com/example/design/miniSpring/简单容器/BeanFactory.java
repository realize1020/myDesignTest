package com.example.design.miniSpring.简单容器;

public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(String name, Class<T> requiredType);

    boolean containsBean(String name);
}
