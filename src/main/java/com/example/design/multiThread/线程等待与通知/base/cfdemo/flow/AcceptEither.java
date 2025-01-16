package com.example.design.multiThread.线程等待与通知.base.cfdemo.flow;


import com.example.design.multiThread.线程等待与通知.tools.SleepTools;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 类说明：取最快消费类
 */
public class AcceptEither {
    public static void main(String[] args) {
        useAcceptEitherPlus();
    }

    private static void useAcceptEither() {
        CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            return "s1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            return "hello world";
        }), (s)-> System.out.println(s));
        SleepTools.second(3);
    }

    private static void useAcceptEitherPlus(){
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            return "s1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            return "hello world";
        });

        CompletableFuture<Void> voidCompletableFuture = future1.acceptEither(future2, s -> System.out.println(s));
        try {
            System.out.println(voidCompletableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
