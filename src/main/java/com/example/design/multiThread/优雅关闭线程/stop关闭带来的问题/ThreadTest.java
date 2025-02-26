package com.example.design.multiThread.优雅关闭线程.stop关闭带来的问题;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 作者：周瑜大都督
 * 因为stop()方法太粗暴了，一旦调用了stop()，就会直接停掉线程，但是调用的时候根本不知道线程刚刚在做什么，任务做到哪一步了，这是很危险的。
 *
 * 这里强调一点，stop()会释放线程占用的synchronized锁（不会自动释放ReentrantLock锁，这也是不建议用stop()的一个因素）。
 */
public class ThreadTest {

    static int count = 0;
    static final Object lock = new Object();
    static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                //synchronized (lock) {
                reentrantLock.lock();
                for (int i = 0; i < 100; i++) {
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                //}
                reentrantLock.unlock();
            }
        });

        thread.start();

        Thread.sleep(5*1000);

        thread.stop();
//
//        Thread.sleep(5*1000);

        reentrantLock.lock();//如果stop方法调用后直接可以释放reentrantLock锁,那么这里就能继续上锁，打印语句
        System.out.println(count);
        reentrantLock.unlock();

//        synchronized (lock) { //如果stop方法调用后直接可以释放synchronized锁，那么这里就会拿到这把锁，然后打印。
//            System.out.println(count);
//        }


    }
}
