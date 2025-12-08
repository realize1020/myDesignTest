package com.example.design.multiThread.读写锁;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ReadLockDemo {
    
    // 模拟ConsumeQueue的状态
    private volatile long maxOffset = 100;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    public static void main(String[] args) throws InterruptedException {
        ReadLockDemo demo = new ReadLockDemo();
        demo.demoRaceCondition();
    }
    
    /**
     * 演示无锁读取的竞态条件问题
     */
    public void demoRaceCondition() throws InterruptedException {
        System.out.println("=== 演示读锁的重要性 ===");
        
        // 测试1：无锁读取（可能出现竞态条件）
        testWithoutLock();
        
        Thread.sleep(1000); // 等待一下
        
        // 测试2：有读锁保护（数据一致性保证）
        testWithReadLock();
    }
    
    /**
     * 测试无锁读取
     */
    private void testWithoutLock() {
        System.out.println("\n--- 测试1：无锁读取（可能出现竞态条件） ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // 消费者线程（无锁读取）
        Runnable consumer = () -> {
            long offset = 100; // 读取最后一个消息
            
            // 模拟不加读锁的情况
            if (offset < 0 || offset >= maxOffset) {
                System.out.println(Thread.currentThread().getName() + ": offset=" + offset + " 无效，maxOffset=" + maxOffset);
                return;
            }
            
            // 模拟耗时操作（让竞态条件更容易出现）
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 再次检查（但数据可能已经变化）
            if (offset >= maxOffset) {
                System.out.println(Thread.currentThread().getName() + ": ⚠️ 竞态条件！offset=" + offset + " 在检查后变为无效，maxOffset=" + maxOffset);
            } else {
                System.out.println(Thread.currentThread().getName() + ": ✅ 成功读取 offset=" + offset);
            }
        };
        
        // 生产者线程（写入新消息）
        Runnable producer = () -> {
            try {
                Thread.sleep(5); // 让消费者先开始
                maxOffset = 101; // 写入新消息，maxOffset增加
                System.out.println(Thread.currentThread().getName() + ": 🚀 生产者写入新消息，maxOffset=" + maxOffset);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // 启动线程
        executor.submit(consumer);
        executor.submit(producer);
        executor.submit(consumer);
        
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 测试有读锁保护
     */
    private void testWithReadLock() {
        System.out.println("\n--- 测试2：有读锁保护（数据一致性保证） ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // 重置状态
        maxOffset = 100;
        
        // 消费者线程（有读锁保护）
        Runnable consumer = () -> {
            long offset = 100;
            
            readWriteLock.readLock().lock();
            try {
                // 在持有读锁期间，数据不会被修改
                if (offset < 0 || offset >= maxOffset) {
                    System.out.println(Thread.currentThread().getName() + ": offset=" + offset + " 无效，maxOffset=" + maxOffset);
                    return;
                }
                
                // 模拟耗时操作
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // 由于持有读锁，maxOffset不会变化
                System.out.println(Thread.currentThread().getName() + ": ✅ 安全读取 offset=" + offset + "，maxOffset=" + maxOffset);
                
            } finally {
                readWriteLock.readLock().unlock();
            }
        };
        
        // 生产者线程（需要获取写锁）
        Runnable producer = () -> {
            try {
                Thread.sleep(5);
                
                // 尝试获取写锁（会被读锁阻塞）
                System.out.println(Thread.currentThread().getName() + ": 🔒 生产者尝试获取写锁...");
                readWriteLock.writeLock().lock();
                try {
                    maxOffset = 101;
                    System.out.println(Thread.currentThread().getName() + ": 🚀 生产者写入新消息，maxOffset=" + maxOffset);
                } finally {
                    readWriteLock.writeLock().unlock();
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // 启动线程
        executor.submit(consumer);
        executor.submit(producer);
        executor.submit(consumer);
        
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}