package com.example.design.aop.aopExample;

import com.example.design.aop.aopExample.retry.MessageService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.design.aop.aopExample")
public class AopTest {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AopTest.class);
        
        MessageService messageService = context.getBean(MessageService.class);
        
        System.out.println("=== 测试日志注解 ===");
        String result1 = messageService.sendMessage("test-topic", "Hello World", 1);
        System.out.println("结果: " + result1);
        
        System.out.println("\n=== 测试重试注解 ===");
        try {
            boolean result2 = messageService.processOrderMessage("ORDER-001", 100.0);
            System.out.println("订单处理结果: " + result2);
        } catch (Exception e) {
            System.out.println("订单处理异常: " + e.getMessage());
        }
        
        System.out.println("\n=== 测试批量处理 ===");
        String[] messages = {"msg1", "msg2", "msg3", "msg4", "msg5"};
        int successCount = messageService.batchProcessMessages(messages);
        System.out.println("成功处理消息数: " + successCount);
        
        context.close();
    }
}