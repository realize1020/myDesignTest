package com.example.design.multiThread.优雅关闭线程;

import java.util.concurrent.CountDownLatch;

public class StopThread1 {
    public static boolean flag = true;
    static CountDownLatch latch =new CountDownLatch(3);
    public static void main(String[] args) throws Exception{
        new Thread(()-> {
            long num = 0;
            while(flag) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "正在运行、" + latch.getCount());
                latch.countDown();
            }
        }, "执行线程").start();

        latch.await();
        flag = false;	// 停止线程

    }
}
