package com.example.design.nacosSource.springcloud监听nacos配置变化并刷新;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MyNacosContextRefresher implements ApplicationListener<ApplicationReadyEvent>,ApplicationContextAware {

    private ApplicationContext applicationContext;

    private AtomicBoolean ready =new AtomicBoolean( false);

    public ConcurrentHashMap    <String, Listener> listenerMap =new ConcurrentHashMap<>(16);
    private final AtomicBoolean isPublishing = new AtomicBoolean(false);


    private static final AtomicLong RefreshCount = new AtomicLong(0);

    public MyNacosContextRefresher(){
        //做一些初始化
        System.out.println("NacosContextRefresher 初始化.....");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if(this.ready.compareAndSet(false,true)){
            //this.registerNacosListenersForApplications();
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void registerNacosListenersForApplications() {
        //做一些参数的判断
        //registerNacosListener("defautGroup", "12345");
    }

    /**
     * 	 * 对应的事件监听器为：RefreshEventListener， Spring Cloud 实现的，
     * 	 * 在该监听器里更新配置和刷新容器中标记了 @RefreshScope 的配置，在 onApplicationEvent 方法中监听2个事件，
     * 	 * ApplicationReadyEvent（spring boot 事件，表示 application 应该初始化完成）、RefreshEvent。
     * 	 * @param groupKey
     * 	 * @param dataKey
     *
     */
    private void registerNacosListener(String defautGroup, String s) {
        String key = defautGroup + ":" + s;
        System.out.println("【NacosContextRefresher】注册监听器，key: " + key);
        Listener listener = listenerMap.computeIfAbsent(key, lis -> new AbstractSharedListener() {
            @Override
            public void innerReceive(String var1, String var2, String var3) {
                System.out.println("【NacosContextRefresher】innerReceive 被调用");
                refreshCountIncrement();
                applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh Nacos config"));
                System.out.println("发送Refresh Nacos config消息");
            }
        });
    }


    public static void refreshCountIncrement() {
        RefreshCount.incrementAndGet();
    }
}

