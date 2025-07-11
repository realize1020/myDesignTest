package com.example.design.nacosSource.springcloud监听nacos配置变化并刷新;

import java.util.concurrent.Executor;

public abstract  class AbstractSharedListener implements Listener{
    private volatile String dataId;
    private volatile String group;

    public AbstractSharedListener() {
    }

    public final void fillContext(String dataId, String group) {
        this.dataId = dataId;
        this.group = group;
    }
    public final void receiveConfigInfo(String configInfo) {
        this.innerReceive(this.dataId, this.group, configInfo);
    }

    public Executor getExecutor() {
        return null;
    }

    public abstract void innerReceive(String var1, String var2, String var3);
}
