package com.example.design.source.sentinelSource.写实复制;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DemoCopyOnWriteMap<K, V> {
    private volatile Map<K, V> map = new HashMap<>();
    
    // 读操作 - 完全无锁
    public V get(K key) {
        return map.get(key);
    }
    
    // 写操作 - 使用双重检查锁和写时复制
    public void put(K key, V value) {
        // 第一次检查（无锁）
        if (!map.containsKey(key)) {
            synchronized (this) {
                // 第二次检查（持有锁）
                if (!map.containsKey(key)) {
                    // 写时复制：创建新Map并复制所有元素
                    Map<K, V> newMap = new HashMap<>(map.size());
                    newMap.putAll(map);
                    newMap.put(key, value);
                    // 原子替换引用
                    map = newMap;
                }
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        DemoCopyOnWriteMap<String, Integer> demo = new DemoCopyOnWriteMap<>();
        
        // 写线程
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.put("key" + i, i);
                System.out.println("Write: key" + i + "=" + i);
            }
        }).start();
        
        // 读线程
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Integer value = demo.get("key" + i);
                System.out.println("Read: key" + i + "=" + value);
            }
        }).start();
    }
}