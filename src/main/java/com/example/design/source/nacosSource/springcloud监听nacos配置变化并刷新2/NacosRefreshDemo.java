//package com.example.design;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.cloud.endpoint.event.RefreshEvent;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContextAware;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 完整的 Nacos 配置刷新 Demo
// * 模仿 Spring Cloud 监听 Nacos 配置中心刷新变化
// */
//@SpringBootApplication
//public class NacosRefreshDemo {
//
//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(NacosRefreshDemo.class, args);
//
//        System.out.println("=== Spring Boot 应用启动完成 ===");
//
//        // 获取 NacosContextRefresher Bean
//        NacosContextRefresher refresher = context.getBean(NacosContextRefresher.class);
//
//        // 等待一下，确保监听器已经注册
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // 模拟配置变更
//        System.out.println("=== 模拟 Nacos 配置变更 ===");
//        Listener listener = refresher.getListenerMap().get("defautGroup:12345");
//        if (listener != null) {
//            listener.innerReceive("12345", "defautGroup", "new config value");
//        } else {
//            System.out.println("未找到监听器，可能注册失败");
//        }
//
//        // 等待事件处理完成
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        System.out.println("=== Demo 结束 ===");
//        context.close();
//    }
//}
//
///**
// * 配置类 - 手动注册 Bean
// */
//@Configuration
//class NacosConfig {
//
//    @Bean
//    public NacosContextRefresher nacosContextRefresher() {
//        return new NacosContextRefresher();
//    }
//}
//
///**
// * 自定义 RefreshEvent 监听器
// */
//@Component
//class YourRefreshEventListener implements ApplicationListener<RefreshEvent> {
//
//    @Override
//    public void onApplicationEvent(RefreshEvent event) {
//        System.out.println("【MyRefreshEventListener】收到 RefreshEvent 事件");
//        System.out.println("【MyRefreshEventListener】事件源: " + event.getSource());
//        System.out.println("【MyRefreshEventListener】事件消息: " + event.getEventDesc());
//        System.out.println("【MyRefreshEventListener】开始刷新配置...");
//
//        // 这里可以添加你的配置刷新逻辑
//        // 比如重新加载配置文件、更新缓存等
//
//        System.out.println("【MyRefreshEventListener】配置刷新完成");
//    }
//}
//
///**
// * 模拟 Nacos 配置监听器
// */
//interface Listener {
//    void innerReceive(String dataId, String group, String configInfo);
//}
//
///**
// * 模拟 AbstractSharedListener
// */
//abstract class AbstractSharedListener implements Listener {
//    @Override
//    public void innerReceive(String dataId, String group, String configInfo) {
//        // 子类实现具体逻辑
//    }
//}
//
///**
// * Nacos 配置刷新器 - 模仿 Spring Cloud Alibaba Nacos
// */
//class NacosContextRefresher implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {
//
//    private ApplicationContext applicationContext;
//    private AtomicBoolean ready = new AtomicBoolean(false);
//    private ConcurrentHashMap<String, Listener> listenerMap = new ConcurrentHashMap<>(16);
//    private static final AtomicLong refreshCount = new AtomicLong(0);
//
//    public NacosContextRefresher() {
//        System.out.println("【NacosContextRefresher】构造函数被调用");
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        System.out.println("【NacosContextRefresher】收到 ApplicationReadyEvent 事件");
//
//        if (this.ready.compareAndSet(false, true)) {
//            System.out.println("【NacosContextRefresher】开始注册 Nacos 监听器");
//            this.registerNacosListenersForApplications();
//        } else {
//            System.out.println("【NacosContextRefresher】监听器已经注册过，跳过");
//        }
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        System.out.println("【NacosContextRefresher】设置 ApplicationContext");
//        this.applicationContext = applicationContext;
//    }
//
//    private void registerNacosListenersForApplications() {
//        System.out.println("【NacosContextRefresher】注册 Nacos 监听器");
//        // 模拟注册监听器
//        registerNacosListener("defautGroup", "12345");
//        System.out.println("【NacosContextRefresher】监听器注册完成");
//    }
//
//    /**
//     * 注册 Nacos 监听器
//     * 对应的事件监听器为：RefreshEventListener， Spring Cloud 实现的，
//     * 在该监听器里更新配置和刷新容器中标记了 @RefreshScope 的配置，在 onApplicationEvent 方法中监听2个事件，
//     * ApplicationReadyEvent（spring boot 事件，表示 application 应该初始化完成）、RefreshEvent。
//     */
//    private void registerNacosListener(String groupKey, String dataKey) {
//        String key = groupKey + ":" + dataKey;
//        System.out.println("【NacosContextRefresher】注册监听器，key: " + key);
//
//        Listener listener = listenerMap.computeIfAbsent(key, lis -> new AbstractSharedListener() {
//            @Override
//            public void innerReceive(String dataId, String group, String configInfo) {
//                System.out.println("【NacosListener】收到配置变更通知");
//                System.out.println("【NacosListener】dataId: " + dataId + ", group: " + group + ", configInfo: " + configInfo);
//
//                refreshCountIncrement();
//
//                // 发布 RefreshEvent 事件
//                System.out.println("【NacosListener】发布 RefreshEvent 事件");
//                applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh Nacos config"));
//
//                System.out.println("【NacosListener】RefreshEvent 事件发布完成");
//            }
//        });
//
//        System.out.println("【NacosContextRefresher】监听器注册成功: " + key);
//    }
//
//    public static void refreshCountIncrement() {
//        long count = refreshCount.incrementAndGet();
//        System.out.println("【NacosContextRefresher】刷新次数: " + count);
//    }
//
//    // Getter 方法，供外部访问
//    public ConcurrentHashMap<String, Listener> getListenerMap() {
//        return listenerMap;
//    }
//}