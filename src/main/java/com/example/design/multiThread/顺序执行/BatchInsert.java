package com.example.design.multiThread.顺序执行;

import java.util.concurrent.CountDownLatch;

/**
 * 多线程执行数据插入
 */
public class BatchInsert {
	public static void main(String[] args) {
		long startTimes = System.currentTimeMillis();
		int threadCount = 10;
		int total = 1000;
		int every = total/threadCount;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i=0;i<threadCount;i++){
			new Thread(new Worker(latch,0,10),"线程"+i).start();
		}
		try {
			latch.await();
			long endTimes = System.currentTimeMillis();
			System.out.println("所有线程执行完毕:" + (endTimes - startTimes));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class Worker implements Runnable{

	int start = 0;
	int end = 0;
	CountDownLatch latch;
	public Worker(CountDownLatch latch, int start,int end){
		this.start = start;
		this.end = end;
		this.latch = latch;
	}

	@Override
	public void run() {
		for (int i = start; i < end; i++) {
			System.out.println("线程" + Thread.currentThread().getName()+ "正在执行。。");
		}
		latch.countDown();
	}

}


