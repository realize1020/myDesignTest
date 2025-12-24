package com.example.design.aop.aopExample.retry;

import com.example.design.aop.aopExample.log.LogExecution;
import com.example.design.aop.aopExample.rateLimit.normal.RateLimit;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 消息服务示例
 * 演示自定义注解的使用
 */
@Service
public class MessageService {
    
    private Random random = new Random();
    
    /**
     * 发送消息 - 使用日志注解
     */
    @LogExecution(level = "INFO", message = "发送消息操作")
    @RateLimit(limit = 10, window = 60, message = "发送消息频率过高")
    public String sendMessage(String topic, String content, int priority) {
        // 模拟业务逻辑
        try {
            TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return "消息发送成功: " + topic + " - " + content;
    }
    
    /**
     * 处理订单消息 - 使用重试和日志注解
     */
    @LogExecution(level = "DEBUG", logParams = true, logResult = true)
    @Retryable(maxAttempts = 3, delay = 1000, exponentialBackoff = true)
    public boolean processOrderMessage(String orderId, double amount) {
        // 模拟可能失败的业务逻辑
        if (random.nextDouble() < 0.3) {
            throw new RuntimeException("订单处理失败: " + orderId);
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(200 + random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return true;
    }
    
    /**
     * 批量处理消息 - 使用所有注解
     */
    @LogExecution(level = "INFO", message = "批量处理消息")
    @Retryable(maxAttempts = 2, delay = 500, retryFor = {RuntimeException.class})
    @RateLimit(limit = 5, window = 30, timeUnit = TimeUnit.SECONDS)
    public int batchProcessMessages(String[] messages) {
        int successCount = 0;
        
        for (String message : messages) {
            try {
                processSingleMessage(message);
                successCount++;
            } catch (Exception e) {
                System.err.println("处理消息失败: " + message + " - " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    /**
     * 私有方法，不会被AOP拦截
     */
    private void processSingleMessage(String message) {
        if (random.nextDouble() < 0.1) {
            throw new RuntimeException("单条消息处理失败");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}