package com.example.design.reflect.JDK动态代理示例;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理完整示例
 */
public class JdkProxyExample {
    
    // 1. 基础接口定义（JDK代理必须基于接口）
    public interface UserService {
        String getUserInfo(String userId);
        void updateUser(String userId, String name);
        default void log(String message) {
            System.out.println("默认方法: " + message);
        }
    }
    
    // 2. 目标实现类
    public static class UserServiceImpl implements UserService {
        @Override
        public String getUserInfo(String userId) {
            System.out.println("实际执行: getUserInfo(" + userId + ")");
            return "用户信息: " + userId;
        }
        
        @Override
        public void updateUser(String userId, String name) {
            System.out.println("实际执行: updateUser(" + userId + ", " + name + ")");
        }
    }
    
    // 3. 另一个接口用于演示多接口代理
    public interface LogService {
        void logOperation(String operation);
    }
    
    public static class UserServiceImpl2 implements UserService, LogService {
        @Override
        public String getUserInfo(String userId) {
            return "UserInfo: " + userId;
        }
        
        @Override
        public void updateUser(String userId, String name) {
            System.out.println("更新用户: " + userId + " -> " + name);
        }
        
        @Override
        public void logOperation(String operation) {
            System.out.println("操作日志: " + operation);
        }
    }
    
    public static void main(String[] args) {
        // 各种JDK动态代理示例
        testBasicProxy();
        testMethodInterceptor();
        testMultiInterfaceProxy();
        testExceptionHandling();
        testPerformanceProxy();
        testRealWorldExample();
    }
    
    /**
     * 1. 基础代理示例
     */
    public static void testBasicProxy() {
        System.out.println("\n=== 1. 基础代理示例 ===");
        
        // 目标对象
        UserService target = new UserServiceImpl();
        
        // 创建代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("【前置处理】方法: " + method.getName());
                    Object result = method.invoke(target, args);
                    System.out.println("【后置处理】方法: " + method.getName());
                    return result;
                }
            }
        );
        
        // 使用代理对象
        String result = proxy.getUserInfo("1001");
        System.out.println("代理调用结果: " + result);
        
        proxy.updateUser("1001", "张三");
        proxy.log("测试默认方法");
    }
    
    /**
     * 2. 方法拦截器（类似Spring AOP）
     */
    public static void testMethodInterceptor() {
        System.out.println("\n=== 2. 方法拦截器示例 ===");
        
        UserService target = new UserServiceImpl();
        
        // 可复用的拦截器
        InvocationHandler interceptor = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                
                // 前置增强
                System.out.println("【AOP前置】" + methodName + " 开始执行");
                long startTime = System.currentTimeMillis();
                
                try {
                    // 执行目标方法
                    Object result = method.invoke(target, args);
                    
                    // 返回后增强
                    System.out.println("【AOP返回】" + methodName + " 执行成功");
                    return result;
                    
                } catch (Exception e) {
                    // 异常增强
                    System.out.println("【AOP异常】" + methodName + " 执行失败: " + e.getMessage());
                    throw e;
                } finally {
                    // 最终增强
                    long endTime = System.currentTimeMillis();
                    System.out.println("【AOP最终】" + methodName + " 执行耗时: " + (endTime - startTime) + "ms");
                }
            }
        };
        
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class},
            interceptor
        );
        
        proxy.getUserInfo("2001");
        proxy.updateUser("2001", "李四");
    }
    
    /**
     * 3. 多接口代理
     */
    public static void testMultiInterfaceProxy() {
        System.out.println("\n=== 3. 多接口代理示例 ===");
        
        UserServiceImpl2 target = new UserServiceImpl2();
        
        Object proxy = Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class, LogService.class}, // 代理多个接口
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理方法: " + method.getDeclaringClass().getSimpleName() 
                                     + "." + method.getName());
                    return method.invoke(target, args);
                }
            }
        );
        
        // 可以转换为任意代理的接口
        UserService userService = (UserService) proxy;
        LogService logService = (LogService) proxy;
        
        userService.getUserInfo("3001");
        logService.logOperation("用户查询操作");
    }
    
    /**
     * 4. 异常处理
     */
    public static void testExceptionHandling() {
        System.out.println("\n=== 4. 异常处理示例 ===");
        
        UserService target = new UserServiceImpl();
        
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    try {
                        System.out.println("开始执行: " + method.getName());
                        return method.invoke(target, args);
                    } catch (Exception e) {
                        // 统一异常处理
                        System.out.println("捕获异常: " + e.getClass().getSimpleName());
                        System.out.println("异常信息: " + e.getCause().getMessage());
                        
                        // 可以返回默认值或抛出业务异常
                        if (method.getReturnType() == String.class) {
                            return "默认返回值";
                        }
                        throw new RuntimeException("业务处理失败", e);
                    }
                }
            }
        );
        
        // 正常调用
        String result = proxy.getUserInfo("4001");
        System.out.println("正常调用结果: " + result);
    }
    
    /**
     * 5. 性能监控代理
     */
    public static void testPerformanceProxy() {
        System.out.println("\n=== 5. 性能监控代理 ===");
        
        UserService target = new UserServiceImpl();
        
        // 性能监控处理器
        InvocationHandler performanceHandler = new InvocationHandler() {
            private int callCount = 0;
            private long totalTime = 0;
            
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                callCount++;
                long startTime = System.nanoTime();
                
                try {
                    return method.invoke(target, args);
                } finally {
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    totalTime += duration;
                    
                    System.out.printf("方法 %s 调用次数: %d, 本次耗时: %d ns, 平均耗时: %.2f ns%n",
                        method.getName(), callCount, duration, (double)totalTime/callCount);
                }
            }
        };
        
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class},
            performanceHandler
        );
        
        // 多次调用观察性能
        for (int i = 0; i < 3; i++) {
            proxy.getUserInfo("500" + i);
        }
    }
    
    /**
     * 6. 实际业务场景示例（类似Spring事务管理）
     */
    public static void testRealWorldExample() {
        System.out.println("\n=== 6. 实际业务场景示例 ===");
        
        // 模拟业务服务
        UserService businessService = new UserServiceImpl();
        
        // 事务管理器
        UserService transactionalProxy = (UserService) Proxy.newProxyInstance(
            businessService.getClass().getClassLoader(),
            new Class[]{UserService.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("【事务开始】");
                    
                    try {
                        // 执行业务方法
                        Object result = method.invoke(businessService, args);
                        System.out.println("【事务提交】");
                        return result;
                        
                    } catch (Exception e) {
                        System.out.println("【事务回滚】原因: " + e.getMessage());
                        throw e;
                    } finally {
                        System.out.println("【资源清理】");
                    }
                }
            }
        );
        
        // 使用事务代理
        transactionalProxy.updateUser("6001", "王五");
    }
    
    /**
     * 7. 高级特性：方法过滤和条件代理
     */
    public static void testAdvancedFeatures() {
        System.out.println("\n=== 7. 高级特性示例 ===");
        
        UserService target = new UserServiceImpl();
        
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            new Class[]{UserService.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    
                    // 方法过滤：只代理特定方法
                    if ("getUserInfo".equals(methodName)) {
                        System.out.println("【增强代理】方法: " + methodName);
                        
                        // 参数预处理
                        if (args != null && args.length > 0) {
                            String userId = (String) args[0];
                            if (!userId.startsWith("USER_")) {
                                args[0] = "USER_" + userId;
                            }
                        }
                        
                        Object result = method.invoke(target, args);
                        
                        // 结果后处理
                        if (result instanceof String) {
                            result = "代理增强: " + result;
                        }
                        
                        return result;
                    } else {
                        // 其他方法直接执行
                        return method.invoke(target, args);
                    }
                }
            }
        );
        
        System.out.println("过滤代理结果: " + proxy.getUserInfo("7001"));
        proxy.updateUser("7001", "直接执行"); // 不会被代理增强
    }
}