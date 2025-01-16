package com.example.design.multiThread.线程等待与通知.base.cfdemo.flow;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：消费类
 */
public class ThenAccept {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "hello")
                .thenAccept(s -> System.out.println(s+" world"));
    }
}
