package com.example.design.multiThread.优雅关闭线程;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StopThread2 {
    public static void main(String[] args) throws Exception{
        List<String> list = Arrays.asList("1", "2", "3");
        // 计数器
        CountDownLatch downLatch = new CountDownLatch(list.size());
        // 创建线程
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(list.size());
            for (String item : list) {
                // 开启线程
                executorService.execute(() -> {
                    try {
                        // 业务处理
                        System.out.println(item);
                    } finally {
                        downLatch.countDown();
                    }
                });
            }
            downLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

}
