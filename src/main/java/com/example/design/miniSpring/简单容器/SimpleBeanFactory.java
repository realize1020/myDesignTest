package com.example.design.miniSpring.简单容器;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory implements BeanFactory {
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    public void registerBean(String name, Object bean) {
        beanMap.put(name, bean);
    }

    @Override
    public Object getBean(String name) {
        return beanMap.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = getBean(name);
        if (requiredType.isInstance(bean)) {
            return requiredType.cast(bean);
        }
        throw new RuntimeException("Bean is not of required type");
    }

    @Override
    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }
}
