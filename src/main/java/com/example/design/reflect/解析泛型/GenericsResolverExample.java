package com.example.design.reflect.解析泛型;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// 定义一个泛型接口
interface GenericInterface<T> {
    void handle(T event);
}

// 实现泛型接口的具体类
class StringEventListener implements GenericInterface<String> {
    @Override
    public void handle(String event) {
        System.out.println("Handling string event: " + event);
    }
}

// 演示泛型解析的工具类
public class GenericsResolverExample {
    
    public static void main(String[] args) {
        // 获取实现类的Class对象
        Class<?> clazz = StringEventListener.class;
        
        // 获取实现的接口数组
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        
        // 假设第一个接口就是我们关心的泛型接口
        if (genericInterfaces.length > 0 && genericInterfaces[0] instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
            
            // 获取泛型参数类型
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            
            if (actualTypeArguments.length > 0) {
                // 输出实际的泛型类型名称
                System.out.println("泛型参数类型: " + actualTypeArguments[0].getTypeName());
                
                try {
                    // 将类型名称转换为Class对象
                    Class<?> eventClass = Class.forName(actualTypeArguments[0].getTypeName());
                    System.out.println("对应的Class对象: " + eventClass.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
