package com.example.design.multiThread.线程等待与通知.base.cfdemo.flow;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：结合转化类
 */
public class ThenCompose {
    public static void main(String[] args) {
        Integer result =
        CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCompose(i -> CompletableFuture
                        .supplyAsync(() -> { return i+1;})).join();
        System.out.println(result);
    }
}
