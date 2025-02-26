package com.example.design.miniSpring.ioc;


/**
 * 可配置的列表化bean工厂接口
 * 提供了配置和列举bean的功能
 *
 * @author kama
 * @version 1.0.0
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 获取bean定义
     *
     * @param beanName bean名称
     * @return bean定义
     * @throws RuntimeException 如果找不到bean定义
     */
    BeanDefinition getBeanDefinition(String beanName) throws RuntimeException;

    /**
     * 预实例化所有单例bean
     *
     * @throws RuntimeException 如果预实例化过程中发生错误
     */
    void preInstantiateSingletons() throws RuntimeException;

    /**
     * 确保所有非延迟加载的单例bean都被实例化
     *
     * @throws RuntimeException 如果实例化过程中发生错误
     */
    void ensureAllSingletonsInstantiated() throws RuntimeException;
} 