package com.example.design.aop.simple;// Application.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        TestService testService = context.getBean(TestService.class);
        
        // 测试正常方法
        String result1 = testService.testMethod1("World");
        System.out.println("返回结果: " + result1);
        
        int result2 = testService.testMethod2(5, 3);
        System.out.println("计算结果: " + result2);
        
        // 测试异常方法
        try {
            testService.testException();
        } catch (Exception e) {
            System.out.println("捕获异常: " + e.getMessage());
        }
        
        context.close();
    }
}
