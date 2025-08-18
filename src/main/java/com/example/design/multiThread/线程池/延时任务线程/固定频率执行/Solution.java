package com.example.design.multiThread.线程池.延时任务线程.固定频率执行;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** scheduleAtFixedRate()方法
 * 定义：以固定的频率执行任务
 * 间隔计算：从上一次任务开始执行的时间点算起
 * 特点：
 * 如果任务执行时间小于间隔时间，任务会按固定频率执行
 * 如果任务执行时间超过间隔时间，下一次任务会立即执行（没有延迟）
 * 保证任务执行的频率相对固定
 */
public class Solution {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("FixedRate-thread", false));

    public static void main(String[] args) {
        scheduledExecutorService.scheduleAtFixedRate(new SheduleRunner(),3000,1000, TimeUnit.MILLISECONDS);





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
            //如果任务执行时间小于间隔时间，任务会按固定频率执行
            System.out.println("线程" + Thread.currentThread().getName() +
                    " 在 " + System.currentTimeMillis() + " 获取最新的服务列表信息......");

            try {
                Thread.sleep(5000);//休眠5s后立即执行下个方法
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
