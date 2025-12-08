package com.example.design.reflect.反射示例;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Java反射实例化完整示例
 */
public class ReflectionExample {
    
    // 测试用的Bean类
    public static class User {
        private String name;
        private int age;
        
        // 无参构造器
        public User() {
            System.out.println("调用无参构造器");
        }
        
        // 有参构造器
        public User(String name, int age) {
            this.name = name;
            this.age = age;
            System.out.println("调用有参构造器: " + name + ", " + age);
        }
        
        // 私有构造器
        private User(String name) {
            this.name = name;
            System.out.println("调用私有构造器: " + name);
        }
        
        // 静态工厂方法
        public static User createUser(String name) {
            System.out.println("调用静态工厂方法: " + name);
            return new User(name, 25);
        }
        
        // 实例方法
        public void sayHello() {
            System.out.println("Hello, I'm " + name + ", " + age + " years old");
        }
        
        // Getter/Setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }
    
    public static void main(String[] args) throws Exception {
        // 各种反射实例化示例
        testNoArgsConstructor();
        testWithArgsConstructor();
        testPrivateConstructor();
        testStaticFactoryMethod();
        testMultipleConstructors();
        testPropertySetting();
    }
    
    /**
     * 1. 无参构造器实例化（最常用）
     */
    public static void testNoArgsConstructor() throws Exception {
        System.out.println("\n=== 1. 无参构造器实例化 ===");
        
        // 获取Class对象
        Class<?> clazz = User.class;
        
        // 方式1: 直接获取无参构造器
        Object user1 = clazz.getDeclaredConstructor().newInstance();
        System.out.println("实例1: " + user1);
        
        // 方式2: 先获取构造器再实例化
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object user2 = constructor.newInstance();
        System.out.println("实例2: " + user2);
    }
    
    /**
     * 2. 有参构造器实例化
     */
    public static void testWithArgsConstructor() throws Exception {
        System.out.println("\n=== 2. 有参构造器实例化 ===");
        
        Class<?> clazz = User.class;
        
        // 获取有参构造器（指定参数类型）
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        
        // 创建实例（传入参数值）
        Object user = constructor.newInstance("张三", 30);
        System.out.println("有参构造实例: " + user);
    }
    
    /**
     * 3. 私有构造器实例化（需要设置可访问性）
     */
    public static void testPrivateConstructor() throws Exception {
        System.out.println("\n=== 3. 私有构造器实例化 ===");
        
        Class<?> clazz = User.class;
        
        // 获取私有构造器
        Constructor<?> privateConstructor = clazz.getDeclaredConstructor(String.class);
        
        // 设置可访问性（绕过private限制）
        privateConstructor.setAccessible(true);
        
        // 创建实例
        Object user = privateConstructor.newInstance("李四");
        System.out.println("私有构造实例: " + user);
    }
    
    /**
     * 4. 静态工厂方法实例化
     */
    public static void testStaticFactoryMethod() throws Exception {
        System.out.println("\n=== 4. 静态工厂方法实例化 ===");
        
        Class<?> clazz = User.class;
        
        // 获取静态方法
        Method factoryMethod = clazz.getDeclaredMethod("createUser", String.class);
        
        // 调用静态方法（第一个参数为null，因为是静态方法）
        Object user = factoryMethod.invoke(null, "王五");
        System.out.println("工厂方法实例: " + user);
    }
    
    /**
     * 5. 多构造器选择实例化（类似Spring的策略）
     */
    public static void testMultipleConstructors() throws Exception {
        System.out.println("\n=== 5. 多构造器选择实例化 ===");
        
        Class<?> clazz = User.class;
        
        // 获取所有构造器
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.println("所有构造器数量: " + constructors.length);
        
        // 模拟Spring的构造器选择逻辑
        Object[] args = new Object[]{"赵六", 28}; // 模拟传入的参数
        
        Constructor<?> constructorToUse = null;
        for (Constructor<?> constructor : constructors) {
            System.out.println("检查构造器: " + constructor);
            System.out.println("参数类型: " + Arrays.toString(constructor.getParameterTypes()));
            
            // 根据参数匹配选择构造器（简化版Spring逻辑）
            if (args != null && constructor.getParameterTypes().length == args.length) {
                constructorToUse = constructor;
                System.out.println("匹配到构造器: " + constructor);
                break;
            }
        }
        
        if (constructorToUse != null) {
            Object user = constructorToUse.newInstance(args);
            System.out.println("选择的构造器实例: " + user);
        } else {
            // 如果没有匹配的构造器，使用无参构造器
            Object user = clazz.getDeclaredConstructor().newInstance();
            System.out.println("使用无参构造器实例: " + user);
        }
    }
    
    /**
     * 6. 属性设置示例（反射设置字段值）
     */
    public static void testPropertySetting() throws Exception {
        System.out.println("\n=== 6. 属性设置示例 ===");
        
        Class<?> clazz = User.class;
        
        // 创建实例
        Object user = clazz.getDeclaredConstructor().newInstance();
        
        // 设置name属性
        java.lang.reflect.Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(user, "钱七");
        
        // 设置age属性
        java.lang.reflect.Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(user, 35);
        
        System.out.println("属性设置后: " + user);
        
        // 调用实例方法
        Method sayHelloMethod = clazz.getDeclaredMethod("sayHello");
        sayHelloMethod.invoke(user);
    }
    
    /**
     * 7. 异常处理示例（类似Spring的错误处理）
     */
    public static void testExceptionHandling() {
        System.out.println("\n=== 7. 异常处理示例 ===");
        
        try {
            Class<?> clazz = User.class;
            
            // 尝试获取不存在的构造器
            Constructor<?> constructor = clazz.getDeclaredConstructor(Integer.class);
            Object user = constructor.newInstance(100);
            
        } catch (Exception e) {
            // 类似Spring的异常包装
            System.out.println("异常类型: " + e.getClass().getSimpleName());
            System.out.println("异常信息: " + e.getMessage());
            
            // Spring会将具体异常包装成BeansException
            // throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}