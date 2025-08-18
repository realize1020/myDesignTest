package com.example.design.multiThread.线程池.延时任务线程.固定的延迟时间执行;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** scheduleWithFixedDelay()方法
 * 定义：以固定的延迟时间执行任务
 * 间隔计算：从上一次任务执行完成的时间点算起
 * 特点：
 * 每次任务执行完毕后，等待指定的延迟时间再执行下一次任务
 * 保证任务之间有固定的间隔时间
 * 不会出现任务重叠执行的情况
 */
public class Solution {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("FixedRate-thread", false));

    public static void main(String[] args) {
        scheduledExecutorService.scheduleWithFixedDelay(new SheduleRunner(),3000,1000, TimeUnit.MILLISECONDS);

//        try {
//            Thread.sleep(10000);
//            scheduledExecutorService.shutdown();
//
//            if(!scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS)){
//                scheduledExecutorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduledExecutorService.shutdownNow();
//        }
    }


    static class SheduleRunner implements Runnable{

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() +
                    " 在 " + System.currentTimeMillis() + " 获取最新的服务列表信息......");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
