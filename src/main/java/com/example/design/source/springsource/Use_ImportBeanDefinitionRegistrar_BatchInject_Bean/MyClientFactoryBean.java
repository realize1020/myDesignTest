// MyClientFactoryBean.java
package com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class MyClientFactoryBean implements FactoryBean<Object> {
    private final String interfaceName;

    public MyClientFactoryBean(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public Object getObject() throws Exception {
        Class<?> clazz = Class.forName(interfaceName);
        // 这里用JDK动态代理简单模拟
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> "你调用了 " + method.getName() + " 方法"
        );
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(interfaceName);
        } catch (Exception e) {
            return null;
        }
    }
}