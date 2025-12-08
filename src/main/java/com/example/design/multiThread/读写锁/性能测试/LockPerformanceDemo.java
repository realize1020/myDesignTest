package com.example.design.multiThread.读写锁.性能测试;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

/**
 * 锁性能测试Demo - 修复版本
 * 比较普通锁和读写锁在不同并发场景下的性能差异
 */
public class LockPerformanceDemo {
    
    // 测试配置（降低操作次数避免阻塞）
    private static final int THREAD_COUNT = 5;           // 降低并发线程数
    private static final int READ_RATIO = 80;           // 读操作比例(%)
    private static final int OPERATION_COUNT = 10000;    // 降低每个线程操作次数
    private static final int WARMUP_ITERATIONS = 100;    // 降低预热迭代次数
    private static final int TIMEOUT_SECONDS = 30;       // 超时时间
    
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
        
        System.out.println("=== 锁性能测试Demo - 修复版本 ===");
        System.out.println("配置: " + THREAD_COUNT + "线程, " + READ_RATIO + "%读操作, " + OPERATION_COUNT + "次操作/线程");
        System.out.println("超时时间: " + TIMEOUT_SECONDS + "秒");
        
        // 预热
        System.out.println("\n--- 预热阶段 ---");
        demo.warmup();
        
        // 测试普通锁
        System.out.println("\n--- 测试普通锁 ---");
        Long normalLockTime = demo.testNormalLock();
        
        if (normalLockTime == null) {
            System.out.println("❌ 普通锁测试超时或失败，跳过后续测试");
            return;
        }
        
        // 重置统计
        demo.resetStats();
        
        // 测试读写锁
        System.out.println("\n--- 测试读写锁 ---");
        Long readWriteLockTime = demo.testReadWriteLock();
        
        if (readWriteLockTime == null) {
            System.out.println("❌ 读写锁测试超时或失败");
            return;
        }
        
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
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.out.println("⚠️ 预热阶段超时，强制关闭线程池");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 测试普通锁性能（带超时机制）
     */
    private Long testNormalLock() {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);
        
        long startTime = System.currentTimeMillis();
        
        // 创建线程池（使用守护线程避免阻塞）
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // 设置为守护线程
            return t;
        });
        
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
                                // 移除耗时操作，避免阻塞
                                // Thread.sleep(0, 100); // 注释掉耗时操作
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            normalLockReadTime.addAndGet(operationTime);
                            normalLockReadCount.incrementAndGet();
                        } else {
                            // 写操作
                            long operationStart = System.nanoTime();
                            
                            synchronized (normalLock) {
                                sharedData++;
                                // 移除耗时操作，避免阻塞
                                // Thread.sleep(0, 500); // 注释掉耗时操作
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            normalLockWriteTime.addAndGet(operationTime);
                            normalLockWriteCount.incrementAndGet();
                        }
                        
                        // 每1000次操作打印进度
                        if (j % 1000 == 0) {
                            System.out.println(Thread.currentThread().getName() + ": 进度 " + j + "/" + OPERATION_COUNT);
                        }
                    }
                    
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": 被中断");
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + ": 发生异常 - " + e.getMessage());
                } finally {
                    endLatch.countDown();
                    System.out.println(Thread.currentThread().getName() + ": 完成");
                }
            });
        }
        
        // 开始测试
        startLatch.countDown();
        System.out.println("测试开始...");
        
        try {
            // 添加超时机制
            boolean completed = endLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            
            if (!completed) {
                System.out.println("❌ 普通锁测试超时 (" + TIMEOUT_SECONDS + "秒)，强制关闭线程池");
                executor.shutdownNow();
                return null;
            }
            
            executor.shutdown();
            
            long totalTime = endTime - startTime;
            System.out.println("✅ 普通锁总耗时: " + totalTime + "ms");
            if (normalLockReadCount.get() > 0) {
                System.out.println("读操作: " + normalLockReadCount.get() + "次, 平均耗时: " + 
                                  (normalLockReadTime.get() / normalLockReadCount.get() / 1000) + "微秒");
            }
            if (normalLockWriteCount.get() > 0) {
                System.out.println("写操作: " + normalLockWriteCount.get() + "次, 平均耗时: " + 
                                  (normalLockWriteTime.get() / normalLockWriteCount.get() / 1000) + "微秒");
            }
            
            return totalTime;
            
        } catch (InterruptedException e) {
            System.out.println("❌ 测试被中断");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * 测试读写锁性能（带超时机制）
     */
    private Long testReadWriteLock() {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);
        
        long startTime = System.currentTimeMillis();
        
        // 创建线程池（使用守护线程避免阻塞）
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // 设置为守护线程
            return t;
        });
        
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
                                // 移除耗时操作，避免阻塞
                                // Thread.sleep(0, 100); // 注释掉耗时操作
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
                                // 移除耗时操作，避免阻塞
                                // Thread.sleep(0, 500); // 注释掉耗时操作
                            } finally {
                                readWriteLock.writeLock().unlock();
                            }
                            
                            long operationTime = System.nanoTime() - operationStart;
                            readWriteLockWriteTime.addAndGet(operationTime);
                            readWriteLockWriteCount.incrementAndGet();
                        }
                        
                        // 每1000次操作打印进度
                        if (j % 1000 == 0) {
                            System.out.println(Thread.currentThread().getName() + ": 进度 " + j + "/" + OPERATION_COUNT);
                        }
                    }
                    
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": 被中断");
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + ": 发生异常 - " + e.getMessage());
                } finally {
                    endLatch.countDown();
                    System.out.println(Thread.currentThread().getName() + ": 完成");
                }
            });
        }
        
        // 开始测试
        startLatch.countDown();
        System.out.println("测试开始...");
        
        try {
            // 添加超时机制
            boolean completed = endLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            
            if (!completed) {
                System.out.println("❌ 读写锁测试超时 (" + TIMEOUT_SECONDS + "秒)，强制关闭线程池");
                executor.shutdownNow();
                return null;
            }
            
            executor.shutdown();
            
            long totalTime = endTime - startTime;
            System.out.println("✅ 读写锁总耗时: " + totalTime + "ms");
            if (readWriteLockReadCount.get() > 0) {
                System.out.println("读操作: " + readWriteLockReadCount.get() + "次, 平均耗时: " + 
                                  (readWriteLockReadTime.get() / readWriteLockReadCount.get() / 1000) + "微秒");
            }
            if (readWriteLockWriteCount.get() > 0) {
                System.out.println("写操作: " + readWriteLockWriteCount.get() + "次, 平均耗时: " + 
                                  (readWriteLockWriteTime.get() / readWriteLockWriteCount.get() / 1000) + "微秒");
            }
            
            return totalTime;
            
        } catch (InterruptedException e) {
            System.out.println("❌ 测试被中断");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            return null;
        }
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