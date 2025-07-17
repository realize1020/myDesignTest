package com.example.design.source.nacosSource.springcloud监听nacos配置变化并刷新;

import org.springframework.context.ApplicationEvent;

public class RefreshEvent extends ApplicationEvent {
    private Object event;
    private String eventDesc;
    public RefreshEvent(Object source,Object event, String eventDesc) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
    }

    public Object getEvent() {
        return this.event;
    }

    public String getEventDesc() {
        return this.eventDesc;
    }
}
