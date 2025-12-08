package com.example.design.multiThread.线程池.延时任务线程.两者延时的区别;

import java.util.Date;
import java.util.concurrent.*;

public class ScheduledDelay {
    public static void main(String[] args) {
        ScheduledExecutorService pool = scheduledExecutorService();
        //定时循环执行，2秒后，每秒执行一次。串行执行，要等前一次执行完才继续
        pool.scheduleWithFixedDelay(() -> {
            //模拟在处理数据
            try {
                Thread.sleep(2000);
                System.out.println(new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    //线程池
    public static ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(
                100,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
