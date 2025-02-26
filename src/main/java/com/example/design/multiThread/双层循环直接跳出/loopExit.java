package com.example.design.multiThread.双层循环直接跳出;

import java.util.concurrent.Semaphore;

/**
 * ThreadPoolExecuter源码addWorker(..)方法中学到
 */
public class loopExit {
    static final Semaphore semaphore =new Semaphore(4);

    public static void main(String[] args) {
        regry:
        for(;;){

            System.out.println("外层循环........");

            for(;;){
                if(!semaphore.tryAcquire()){
                    semaphore.release();
                    break regry;//regrey关键字直接跳转到regrey位置，跳出外层循环
                    //break; //•break;：只终止最内层的循环，外层循环的剩余部分仍然会被执行。
                    //continue regry; // •continue retry;：跳过当前外层循环的剩余部分，直接进入下一次外层循环迭代。
                }
                System.out.println("内层循环........");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("结束");
    }
}
