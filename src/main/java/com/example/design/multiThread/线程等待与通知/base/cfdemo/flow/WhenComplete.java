package com.example.design.multiThread.线程等待与通知.base.cfdemo.flow;


import com.example.design.multiThread.线程等待与通知.tools.SleepTools;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：运行后记录结果类
 */
public class WhenComplete {
    public static void main(String[] args) {
        String result = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).whenComplete((s, t) -> {
            System.out.println(s);
            System.out.println(t.getMessage());
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world";
        }).join();
        System.out.println(result);
    }
}
