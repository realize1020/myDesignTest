package com.example.design.multiThread.线程等待与通知.base.cfdemo.flow;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：变换类
 */
public class ThenApply {
    public static void main(String[] args) {
        String result = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(s -> s + " world").join();
        System.out.println(result);
    }
}
