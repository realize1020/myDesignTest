package com.example.design.multiThread.顺序执行;

import java.util.concurrent.CountDownLatch;

public class BatchInsert3 {
    static volatile boolean begin = false;
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch1 =new CountDownLatch(1);
        CountDownLatch latch2 =new CountDownLatch(1);
        CountDownLatch latch3 =new CountDownLatch(1);

        Thread workThread1 = new Thread(new Worker3(0,10,latch1),"线程1");
        Thread workThread2 = new Thread(new Worker3(0,10,latch2),"线程2");
        Thread workThread3 = new Thread(new Worker3(0,10,latch3),"线程3");
        Thread workThread4 = new Thread(new Worker3(0,10,latch3),"线程4");



        workThread1.start();
        while(workThread1.isAlive()){

        }
        workThread2.start();

        while(workThread2.isAlive()){

        }
        workThread3.start();

        while(workThread3.isAlive()){

        }
        workThread4.start();


    }

}


class Worker3 implements Runnable{

    int start = 0;
    int end = 0;
    CountDownLatch latch;
    public Worker3(int start, int end,CountDownLatch latch){
        this.start = start;
        this.end = end;
        this.latch=latch;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            System.out.println("线程" + Thread.currentThread().getName()+ "正在执行。。");
        }
        latch.countDown();

    }

}
