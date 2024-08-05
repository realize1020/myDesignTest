package com.example.design.multiThread.顺序执行;

import java.util.concurrent.CountDownLatch;

public class BatchInsert2 {
    public static void main(String[] args) {
        long startTimes = System.currentTimeMillis();
        int threadCount = 10;
        int total = 1000;
        int every = total/threadCount;
        final CountDownLatch latch = new CountDownLatch(1);
        for(int i=0;i<threadCount;i++){
            new Thread(new Worker2(latch,0,10),"线程"+i).start();
        }
        long endTimes = System.currentTimeMillis();
        System.out.println("所有线程执行完毕:" + (endTimes - startTimes));
        latch.countDown();

    }
}

class Worker2 implements Runnable{

    int start = 0;
    int end = 0;
    CountDownLatch latch;
    public Worker2(CountDownLatch latch, int start,int end){
        this.start = start;
        this.end = end;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程" + Thread.currentThread().getName()+ "正在执行。。");
        }
    }

}
