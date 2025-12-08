package com.example.design.reflect.JDK动态代理示例.MyBatis风格的无实现类Mapper代理示例;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis风格的无实现类Mapper代理示例
 */
public class MapperProxyExample {
    
    // 1. 模拟MyBatis的Mapper接口（没有实现类）
    public interface UserMapper {
        User getUserById(Long id);
        int insertUser(User user);
        int updateUser(User user);
        int deleteUser(Long id);
    }
    
    // 2. 实体类
    public static class User {
        private Long id;
        private String name;
        private Integer age;
        
        public User() {}
        
        public User(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
        
        // Getter/Setter
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', age=" + age + "}";
        }
    }
    
    // 3. 模拟MyBatis的SqlSession
    public static class MockSqlSession {
        private Map<String, Object> methodHandlers = new HashMap<>();
        
        public MockSqlSession() {
            // 注册方法处理器（模拟MyBatis的MappedStatement）
            methodHandlers.put("getUserById", new GetUserHandler());
            methodHandlers.put("insertUser", new InsertUserHandler());
            methodHandlers.put("updateUser", new UpdateUserHandler());
            methodHandlers.put("deleteUser", new DeleteUserHandler());
        }
        
        public <T> T getMapper(Class<T> mapperInterface) {
            // 关键：创建没有实现类的代理
            return (T) Proxy.newProxyInstance(
                mapperInterface.getClassLoader(),
                new Class[]{mapperInterface},
                new MapperProxyHandler(this)
            );
        }
        
        public Object execute(String methodName, Object[] args) {
            MethodHandler handler = (MethodHandler) methodHandlers.get(methodName);
            if (handler != null) {
                return handler.handle(args);
            }
            throw new RuntimeException("未找到方法处理器: " + methodName);
        }
    }
    
    // 4. Mapper代理处理器（核心）
    public static class MapperProxyHandler implements InvocationHandler {
        private MockSqlSession sqlSession;
        
        public MapperProxyHandler(MockSqlSession sqlSession) {
            this.sqlSession = sqlSession;
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            
            System.out.println("【Mapper代理】拦截方法: " + methodName);
            System.out.println("  返回类型: " + returnType);
            System.out.println("  参数: " + java.util.Arrays.toString(args));
            
            // 关键：这里没有调用method.invoke(target, args)
            // 因为没有具体的实现类对象！
            
            // 而是将方法调用转发给SqlSession执行
            return sqlSession.execute(methodName, args);
        }
    }
    
    // 5. 方法处理器接口
    public interface MethodHandler {
        Object handle(Object[] args);
    }
    
    // 6. 具体的方法处理器实现
    public static class GetUserHandler implements MethodHandler {
        private Map<Long, User> userDatabase = new HashMap<>();
        
        public GetUserHandler() {
            // 模拟数据库数据
            userDatabase.put(1L, new User(1L, "张三", 25));
            userDatabase.put(2L, new User(2L, "李四", 30));
        }
        
        @Override
        public Object handle(Object[] args) {
            Long id = (Long) args[0];
            System.out.println("【SQL执行】SELECT * FROM user WHERE id = " + id);
            return userDatabase.get(id);
        }
    }
    
    public static class InsertUserHandler implements MethodHandler {
        @Override
        public Object handle(Object[] args) {
            User user = (User) args[0];
            System.out.println("【SQL执行】INSERT INTO user VALUES(" + user.getId() + ", '" + user.getName() + "', " + user.getAge() + ")");
            return 1; // 返回影响行数
        }
    }
    
    public static class UpdateUserHandler implements MethodHandler {
        @Override
        public Object handle(Object[] args) {
            User user = (User) args[0];
            System.out.println("【SQL执行】UPDATE user SET name = '" + user.getName() + "', age = " + user.getAge() + " WHERE id = " + user.getId());
            return 1;
        }
    }
    
    public static class DeleteUserHandler implements MethodHandler {
        @Override
        public Object handle(Object[] args) {
            Long id = (Long) args[0];
            System.out.println("【SQL执行】DELETE FROM user WHERE id = " + id);
            return 1;
        }
    }
    
    public static void main(String[] args) {
        // 模拟MyBatis的使用方式
        MockSqlSession sqlSession = new MockSqlSession();
        
        // 关键：这里只有接口，没有实现类！
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        
        System.out.println("=== MyBatis风格Mapper代理演示 ===\n");
        
        // 1. 查询用户
        User user = userMapper.getUserById(1L);
        System.out.println("查询结果: " + user);
        System.out.println();
        
        // 2. 插入用户
        User newUser = new User(3L, "王五", 28);
        int insertResult = userMapper.insertUser(newUser);
        System.out.println("插入结果: " + insertResult);
        System.out.println();
        
        // 3. 更新用户
        newUser.setName("王五(已更新)");
        int updateResult = userMapper.updateUser(newUser);
        System.out.println("更新结果: " + updateResult);
        System.out.println();
        
        // 4. 删除用户
        int deleteResult = userMapper.deleteUser(2L);
        System.out.println("删除结果: " + deleteResult);
    }
}