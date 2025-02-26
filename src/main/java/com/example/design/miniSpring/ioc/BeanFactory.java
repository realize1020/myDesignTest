package com.example.design.miniSpring.ioc;

/**
 * Bean工厂接口
 * 定义获取bean的基本方法
 *
 * @author kama
 * @version 1.0.0
 */
public interface BeanFactory {

    /**
     * 根据bean名称获取bean实例
     *
     * @param name bean名称
     * @return bean实例
     * @throws RuntimeException 如果无法获取bean
     */
    Object getBean(String name) throws RuntimeException;

    /**
     * 根据bean名称和类型获取bean实例
     *
     * @param name bean名称
     * @param requiredType 需要的bean类型
     * @return bean实例
     * @throws RuntimeException 如果无法获取bean
     */
    <T> T getBean(String name, Class<T> requiredType) throws RuntimeException;

    /**
     * 根据类型获取bean实例
     *
     * @param requiredType 需要的bean类型
     * @return bean实例
     * @throws RuntimeException 如果无法获取bean
     */
    <T> T getBean(Class<T> requiredType) throws RuntimeException;

    /**
     * 判断是否包含指定名称的bean
     *
     * @param name bean名称
     * @return 如果包含返回true，否则返回false
     */
    boolean containsBean(String name);

    /**
     * 判断指定名称的bean是否是单例
     *
     * @param name bean名称
     * @return 如果是单例返回true，否则返回false
     * @throws RuntimeException 如果无法判断bean的作用域
     */
    boolean isSingleton(String name) throws RuntimeException;

    /**
     * 判断指定名称的bean是否是原型
     *
     * @param name bean名称
     * @return 如果是原型返回true，否则返回false
     * @throws RuntimeException 如果无法判断bean的作用域
     */
    boolean isPrototype(String name) throws RuntimeException;

    /**
     * 获取指定名称bean的类型
     *
     * @param name bean名称
     * @return bean的类型，如果不存在返回null
     * @throws RuntimeException 如果无法获取bean的类型
     */
    Class<?> getType(String name) throws RuntimeException;
} 