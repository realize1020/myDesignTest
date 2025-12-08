package com.example.design.aop.simple;// TestLocalTaskMessageService.java
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @LoggingAnnotation("测试方法1")
    public String testMethod1(String param) {
        try {
            Thread.sleep(100); // 模拟业务处理
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Hello, " + param;
    }

    @LoggingAnnotation("测试方法2")
    public int testMethod2(int a, int b) {
        return a + b;
    }

    @LoggingAnnotation("异常测试方法")
    public void testException() {
        throw new RuntimeException("这是一个测试异常");
    }
}
