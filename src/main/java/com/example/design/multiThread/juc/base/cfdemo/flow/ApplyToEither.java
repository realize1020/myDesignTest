package com.example.design.multiThread.juc.base.cfdemo.flow;


import com.example.design.multiThread.juc.tools.SleepTools;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：取最快转换类
 */
public class ApplyToEither {
    public static void main(String[] args) {
        String result = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(2);
            return "s1";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            return "hello world";
        }), s ->s).join();
        System.out.println(result);
    }
}
