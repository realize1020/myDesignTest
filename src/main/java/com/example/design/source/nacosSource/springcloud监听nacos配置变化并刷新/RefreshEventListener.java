package com.example.design.source.nacosSource.springcloud监听nacos配置变化并刷新;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * springcloud里面的
 * 监听器会调用 Spring Cloud 的刷新机制，重新加载配置，并刷新所有 @RefreshScope 的 Bean。
 * 这样，Nacos 配置中心的变更就能实时反映到应用中
 */
@Component("myRefreshEventListener")  // 指定不同的 Bean 名称
public class RefreshEventListener implements SmartApplicationListener {
    private AtomicBoolean ready = new AtomicBoolean(false);
    private AtomicBoolean isrefresh = new AtomicBoolean(false);
    private ContextRefresher refresh;

    public RefreshEventListener(ContextRefresher refresh) {
        this.refresh = refresh;
    }
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType) ||
                RefreshEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("监听到事件：" + event.getClass().getSimpleName());
        if(event instanceof ApplicationReadyEvent){
            handle((ApplicationReadyEvent)  event);
        }else if(event instanceof RefreshEvent){
            handle((RefreshEvent) event);
        }
    }
    public void handle(ApplicationReadyEvent event) {
        System.out.println("ApplicationReadyEvent触发，开始刷新...");
        this.ready.compareAndSet(false, true);
    }
    private void handle(RefreshEvent event) {
        // 如果已经在刷新，直接返回，阻止递归
        if (!isrefresh.compareAndSet(false, true)) {
            System.out.println("递归刷新被阻止！");
            return;
        }
        try {
            Set<String> keys = this.refresh.refresh();
            System.out.println("刷新了" + keys.size() + "个bean");
            for (String key : keys) {
                System.out.println("刷新了"+key);
            }
        } finally {
            isrefresh.set(false); // 恢复标志
        }
    }
}
