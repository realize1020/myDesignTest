package com.example.design.multiThread.线程池.延时任务线程;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 开始后启动一个定时任务，每隔3秒执行一次，获取最新的服务列表信息
 */
public class Solution {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("delay-circle-thread", false));

    public static ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public static void main(String[] args) {

        scheduledExecutorService.schedule(new SheduleRunner(getScheduledExecutorService()),3000, TimeUnit.MILLISECONDS);
    }


    static class SheduleRunner implements Runnable{

        private  ScheduledExecutorService scheduledExecutorService;

        public SheduleRunner(ScheduledExecutorService scheduledExecutorService) {
            this.scheduledExecutorService=scheduledExecutorService;
        }

        @Override
        public void run() {
            System.out.println("获取最新的服务列表信息......");
            scheduledExecutorService.schedule(this,3000,TimeUnit.MILLISECONDS);
        }


    }


}



