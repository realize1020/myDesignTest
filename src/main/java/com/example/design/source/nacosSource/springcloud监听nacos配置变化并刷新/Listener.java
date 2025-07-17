package com.example.design.source.nacosSource.springcloud监听nacos配置变化并刷新;

import java.util.concurrent.Executor;

public interface Listener {
    Executor getExecutor();

    void receiveConfigInfo(String var1);
}
