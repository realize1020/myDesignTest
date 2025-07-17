package com.example.design.source.feignSource;

import java.lang.reflect.*;
import java.util.*;


/**
 * 结构说明
 * 你调用 userClient.getUserById(123L)，会被 JDK 代理拦截，转发到 FeignInvocationHandler。
 * FeignInvocationHandler 根据方法找到对应的 SynchronousMethodHandler，调用其 invoke。
 * SynchronousMethodHandler 组装请求，调用底层 SimpleClient，模拟 HTTP 调用。
 * 整个流程和 Feign 的真实结构高度相似！
 */

// 1. 用户接口
interface UserClient {
    String getUserById(Long id);
    String hello(String name);
}

// 2. MethodHandler接口
interface MethodHandler {
    Object invoke(Object[] args) throws Throwable;
}

// 3. 模拟底层Client
class SimpleClient {
    public String execute(String methodName, Object[] args) {
        // 模拟HTTP请求
        return "[HTTP] 调用方法: " + methodName + ", 参数: " + Arrays.toString(args);
    }
}

// 4. SynchronousMethodHandler实现
class SynchronousMethodHandler implements MethodHandler {
    private final String methodName;
    private final SimpleClient client;

    public SynchronousMethodHandler(String methodName, SimpleClient client) {
        this.methodName = methodName;
        this.client = client;
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {
        // 组装请求并调用底层Client
        return client.execute(methodName, args);
    }
}

// 5. FeignInvocationHandler
class FeignInvocationHandler implements InvocationHandler {
    private final Map<Method, MethodHandler> dispatch;

    public FeignInvocationHandler(Map<Method, MethodHandler> dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理Object的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        // 调用对应的MethodHandler
        return dispatch.get(method).invoke(args);
    }
}

// 6. Feign类，负责生成代理
class Feign {
    public static <T> T newInstance(Class<T> apiType, SimpleClient client) {
        Map<Method, MethodHandler> dispatch = new HashMap<>();
        for (Method method : apiType.getMethods()) {
            dispatch.put(method, new SynchronousMethodHandler(method.getName(), client));
        }
        FeignInvocationHandler handler = new FeignInvocationHandler(dispatch);
        // 创建JDK动态代理
        Object proxy = Proxy.newProxyInstance(
                apiType.getClassLoader(),
                new Class<?>[]{apiType},
                handler
        );
        return apiType.cast(proxy);
    }
}

// 7. 测试
public class FeignDemo {
    public static void main(String[] args) {
        SimpleClient client = new SimpleClient();
        UserClient userClient = Feign.newInstance(UserClient.class, client);

        // 调用接口方法，实际走到SynchronousMethodHandler
        System.out.println(userClient.getUserById(123L));
        System.out.println(userClient.hello("Alice"));
    }
}