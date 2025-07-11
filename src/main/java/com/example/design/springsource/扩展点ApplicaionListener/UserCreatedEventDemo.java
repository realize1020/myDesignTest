package com.example.design.springsource.扩展点ApplicaionListener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 用户创建事件 Demo
 * 演示如何使用 Spring 事件机制实现解耦的业务处理
 */
@Component
public class UserCreatedEventDemo {

    public static void main(String[] args) {
        // 创建 Spring 上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(UserService.class);
        context.register(EventConfig.class);
        //context.refresh();

        // 获取用户服务
        UserService userService = context.getBean(UserService.class);

        // 创建用户，会触发事件
        System.out.println("=== 开始创建用户 ===");
        User user = new User(1L, "张三", "zhangsan@example.com", "13800138000");
        userService.createUser(user);
        
        // 等待异步处理完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        context.close();
    }
}

/**
 * 用户实体类
 */
class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createTime;

    public User(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', phone='" + phone + "'}";
    }
}

/**
 * 用户创建事件
 */
class UserCreatedEvent extends ApplicationEvent {
    private final User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

/**
 * 用户服务 - 事件发布者
 */
@Component
class UserService {
    
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    public UserService(org.springframework.context.ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 创建用户 - 发布事件
     */
    public void createUser(User user) {
        System.out.println("【用户服务】开始创建用户: " + user.getName());
        
        // 模拟数据库保存
        try {
            Thread.sleep(100);
            System.out.println("【用户服务】用户保存到数据库成功: " + user.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 发布用户创建事件
        System.out.println("【用户服务】发布用户创建事件");
        eventPublisher.publishEvent(new UserCreatedEvent(this, user));
        
        System.out.println("【用户服务】用户创建流程完成");
    }
}

/**
 * 邮件服务监听者 - 发送欢迎邮件
 */
@Component
class EmailService implements ApplicationListener<UserCreatedEvent> {
    
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("【邮件服务】收到用户创建事件，开始发送欢迎邮件给: " + user.getEmail());
        
        // 模拟异步发送邮件
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
                System.out.println("【邮件服务】欢迎邮件发送成功: " + user.getEmail());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("【邮件服务】邮件发送被中断");
            }
        });
    }
}

/**
 * 短信服务监听者 - 发送欢迎短信
 */
@Component
class SmsService implements ApplicationListener<UserCreatedEvent> {
    
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("【短信服务】收到用户创建事件，开始发送欢迎短信给: " + user.getPhone());
        
        // 模拟异步发送短信
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(300);
                System.out.println("【短信服务】欢迎短信发送成功: " + user.getPhone());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("【短信服务】短信发送被中断");
            }
        });
    }
}

/**
 * 日志服务监听者 - 记录用户创建日志
 */
@Component
class LogService implements ApplicationListener<UserCreatedEvent> {
    
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("【日志服务】收到用户创建事件，记录用户创建日志");
        
        // 模拟记录日志
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
                System.out.println("【日志服务】用户创建日志记录成功: " + user.getName() + " 创建时间: " + user.getCreateTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("【日志服务】日志记录被中断");
            }
        });
    }
}

/**
 * 统计服务监听者 - 更新用户统计
 */
@Component
class StatisticsService implements ApplicationListener<UserCreatedEvent> {
    
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("【统计服务】收到用户创建事件，更新用户统计数据");
        
        // 模拟更新统计
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(200);
                System.out.println("【统计服务】用户统计数据更新成功，总用户数+1");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("【统计服务】统计更新被中断");
            }
        });
    }
}

/**
 * 缓存服务监听者 - 更新用户缓存
 */
@Component
class CacheService implements ApplicationListener<UserCreatedEvent> {
    
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("【缓存服务】收到用户创建事件，更新用户缓存");
        
        // 模拟更新缓存
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(150);
                System.out.println("【缓存服务】用户缓存更新成功: " + user.getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("【缓存服务】缓存更新被中断");
            }
        });
    }
}

/**
 * Spring 配置类
 */
@Configuration
class EventConfig {
    
    @Bean
    public org.springframework.context.ApplicationEventPublisher applicationEventPublisher(
            AnnotationConfigApplicationContext context) {
        return context;
    }
}