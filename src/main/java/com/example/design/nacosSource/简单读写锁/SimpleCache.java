package com.example.design.nacosSource.简单读写锁;

public class SimpleCache {
    private final SimpleReadWriteLock lock = new SimpleReadWriteLock();
    private String value;

    // 读操作
    public String get() {
        if (lock.tryReadLock()) {
            try {
                // 临界区：读取缓存
                return value;
            } finally {
                lock.releaseReadLock();
            }
        } else {
            // 获取读锁失败，直接返回null或重试
            return null;
        }
    }

    // 写操作
    public void set(String newValue) {
        if (lock.tryWriteLock()) {
            try {
                // 临界区：写入缓存
                value = newValue;
            } finally {
                lock.releaseWriteLock();
            }
        } else {
            // 获取写锁失败，可以选择重试或报错
            throw new IllegalStateException("Write lock not available");
        }
    }
}