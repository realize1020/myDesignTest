package com.example.design.multiThread.读写锁;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

/**
 * 锁性能测试Demo
 * 比较普通锁和读写锁在不同并发场景下的性能差异
 */
public class LockPerformanceDemo {
    
    // 测试配置
    private static final int THREAD_COUNT = 10;          // 并发线程数
    private static final int READ_RATIO = 80;           // 读操作比例(%)
    private static final int OPERATION_COUNT = 100000;   // 每个线程操作次数
    private static final int WARMUP_ITERATIONS = 1000;   // 预热迭代次数
    
    // 共享数据
    private volatile int sharedData = 0;
    
    // 锁对象
    private final Object normalLock = new Object();      // 普通锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); // 读写锁
    
    // 统计信息
    private final AtomicLong normalLockReadTime = new AtomicLong(0);
    private final AtomicLong normalLockWriteTime = new AtomicLong(0);
    private final AtomicLong readWriteLockReadTime = new AtomicLong(0);
    private final AtomicLong readWriteLockWriteTime = new AtomicLong(0);
    private final AtomicInteger normalLockReadCount = new AtomicInteger(0);
    private final AtomicInteger normalLockWriteCount = new AtomicInteger(0);
    private final AtomicInteger readWriteLockReadCount = new AtomicInteger(0);
    private final AtomicInteger readWriteLockWriteCount = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        LockPerformanceDemo demo = new LockPerformanceDemo();
        
        System.out.println("=== 锁性能测试Demo ===");
        System.out.println("配置: " + THREAD_COUNT + "线程, " + READ_RATIO + "%读操作, " + OPERATION_COUNT + "次操作/线程");
        
        // 预热
        System.out.println("\n--- 预热阶段 ---");
        demo.warmup();
        
        // 测试普通锁
        System.out.println("\n--- 测试普通锁 ---");
        long normalLockTime = demo.testNormalLock();
        
        // 重置统计
        demo.resetStats();
        
        // 测试读写锁
        System.out.println("\n--- 测试读写锁 ---");
        long readWriteLockTime = demo.testReadWriteLock();
        
        // 性能对比分析
        System.out.println("\n=== 性能对比分析 ===");
        demo.analyzeResults(normalLockTime, readWriteLockTime);
    }
    
    /**
     * 预热JVM
     */
    private void warmup() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            executor.submit(() -> {
                synchronized (normalLock) {
                    sharedData++;
                }
            });
            
            executor.submit(() -> {
                readWriteLock.readLock().lock();
                try {
                    int value = sharedData;
                } finally {
                    readWriteLock.readLock().unlock();
                }
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 测试普通锁性能
     */
    private long testNormalLock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);
        
        long startTime = System.currentTimeMillis();
        
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待开始信号
                    
                    for (int j = 0; j < OPERATION_COUNT; j++) {
                        if (ThreadLocalRandom.current().nextInt(100) < READ_RATIO) {
                            // 读操作
                            long operationStart = System.nanoTime();
                            
                            synchronized (normalLock) {
                                int value = sharedData;
                                // 模拟读操作耗时
                                Thread.sleep(0, 100); // 100纳秒
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            normalLockReadTime.addAndGet(operationTime);
                            normalLockReadCount.incrementAndGet();
                        } else {
                            // 写操作
                            long operationStart = System.nanoTime();
                            
                            synchronized (normalLock) {
                                sharedData++;
                                // 模拟写操作耗时
                                Thread.sleep(0, 500); // 500纳秒
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            normalLockWriteTime.addAndGet(operationTime);
                            normalLockWriteCount.incrementAndGet();
                        }
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // 开始测试
        startLatch.countDown();
        
        // 等待所有线程完成
        endLatch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        
        long totalTime = endTime - startTime;
        System.out.println("普通锁总耗时: " + totalTime + "ms");
        System.out.println("读操作: " + normalLockReadCount.get() + "次, 平均耗时: " + 
                          (normalLockReadTime.get() / normalLockReadCount.get() / 1000) + "微秒");
        System.out.println("写操作: " + normalLockWriteCount.get() + "次, 平均耗时: " + 
                          (normalLockWriteTime.get() / normalLockWriteCount.get() / 1000) + "微秒");
        
        return totalTime;
    }
    
    /**
     * 测试读写锁性能
     */
    private long testReadWriteLock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);
        
        long startTime = System.currentTimeMillis();
        
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待开始信号
                    
                    for (int j = 0; j < OPERATION_COUNT; j++) {
                        if (ThreadLocalRandom.current().nextInt(100) < READ_RATIO) {
                            // 读操作
                            long operationStart = System.nanoTime();
                            
                            readWriteLock.readLock().lock();
                            try {
                                int value = sharedData;
                                // 模拟读操作耗时
                                Thread.sleep(0, 100); // 100纳秒
                            } finally {
                                readWriteLock.readLock().unlock();
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            readWriteLockReadTime.addAndGet(operationTime);
                            readWriteLockReadCount.incrementAndGet();
                        } else {
                            // 写操作
                            long operationStart = System.nanoTime();
                            
                            readWriteLock.writeLock().lock();
                            try {
                                sharedData++;
                                // 模拟写操作耗时
                                Thread.sleep(0, 500); // 500纳秒
                            } finally {
                                readWriteLock.writeLock().unlock();
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            readWriteLockWriteTime.addAndGet(operationTime);
                            readWriteLockWriteCount.incrementAndGet();
                        }
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // 开始测试
        startLatch.countDown();
        
        // 等待所有线程完成
        endLatch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        
        long totalTime = endTime - startTime;
        System.out.println("读写锁总耗时: " + totalTime + "ms");
        System.out.println("读操作: " + readWriteLockReadCount.get() + "次, 平均耗时: " + 
                          (readWriteLockReadTime.get() / readWriteLockReadCount.get() / 1000) + "微秒");
        System.out.println("写操作: " + readWriteLockWriteCount.get() + "次, 平均耗时: " + 
                          (readWriteLockWriteTime.get() / readWriteLockWriteCount.get() / 1000) + "微秒");
        
        return totalTime;
    }
    
    /**
     * 重置统计信息
     */
    private void resetStats() {
        normalLockReadTime.set(0);
        normalLockWriteTime.set(0);
        readWriteLockReadTime.set(0);
        readWriteLockWriteTime.set(0);
        normalLockReadCount.set(0);
        normalLockWriteCount.set(0);
        readWriteLockReadCount.set(0);
        readWriteLockWriteCount.set(0);
        sharedData = 0;
    }
    
    /**
     * 分析测试结果
     */
    private void analyzeResults(long normalLockTime, long readWriteLockTime) {
        double improvement = ((double) (normalLockTime - readWriteLockTime) / normalLockTime) * 100;
        
        System.out.println("性能对比:");
        System.out.println("普通锁总耗时: " + normalLockTime + "ms");
        System.out.println("读写锁总耗时: " + readWriteLockTime + "ms");
        
        if (readWriteLockTime < normalLockTime) {
            System.out.println("✅ 读写锁性能提升: " + String.format("%.2f", improvement) + "%");
        } else {
            System.out.println("❌ 读写锁性能下降: " + String.format("%.2f", -improvement) + "%");
        }
        
        // 详细分析
        System.out.println("\n详细分析:");
        System.out.println("1. 读操作比例: " + READ_RATIO + "% (高读场景)");
        System.out.println("2. 并发线程数: " + THREAD_COUNT);
        System.out.println("3. 读写锁优势场景: 读多写少的高并发环境");
        
        if (READ_RATIO > 50) {
            System.out.println("✅ 当前测试场景适合使用读写锁");
        } else {
            System.out.println("⚠️ 当前测试场景可能更适合使用普通锁");
        }
    }
}