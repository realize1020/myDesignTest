package com.example.design.multiThread.juc.并发工具类.sync;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ConcurrentHashMapDemo {
    
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        ConcurrentHashMapDemo demo = new ConcurrentHashMapDemo();
        demo.runDemo();
    }
    
    public void runDemo() {
        System.out.println("=== 1. 缓存初始化（避免重复计算）===");
        demoCacheInitialization();
        
        System.out.println("\n=== 2. 计数器累加（避免读改写竞态）===");
        demoCounterIncrement();
        
        System.out.println("\n=== 3. 对象池/锁池（避免重复创建）===");
        demoObjectPool();
        
        System.out.println("\n=== 4. 条件更新（避免检查再行动）===");
        demoConditionalUpdate();
        
        System.out.println("\n=== 5. 合并操作（避免复杂读改写）===");
        demoMergeOperation();
    }
    
    /**
     * 场景1：缓存初始化 - 避免重复计算
     * 错误写法：if (!cache.containsKey(key)) { cache.put(key, expensiveCompute(key)); }
     */
    public void demoCacheInitialization() {
        String key = "expensive_data";
        
        // ❌ 错误写法：有竞态条件
        // if (!cache.containsKey(key)) {
        //     cache.put(key, expensiveCompute(key));
        // }
        
        // ✅ 正确写法：使用 computeIfAbsent
        String result = cache.computeIfAbsent(key, this::expensiveCompute);
        System.out.println("缓存结果: " + result);
        
        // 再次获取，不会重复计算
        String cached = cache.computeIfAbsent(key, this::expensiveCompute);
        System.out.println("缓存命中: " + cached);
    }
    
    private String expensiveCompute(String key) {
        System.out.println("执行昂贵计算: " + key);
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return "computed_" + key + "_" + System.currentTimeMillis();
    }
    
    /**
     * 场景2：计数器累加 - 避免读改写竞态
     * 错误写法：counters.put(key, counters.get(key) + 1);
     */
    public void demoCounterIncrement() {
        String key = "request_count";
        
        // ❌ 错误写法：读改写竞态
        // AtomicInteger count = counters.get(key);
        // if (count == null) {
        //     count = new AtomicInteger(0);
        //     counters.put(key, count);
        // }
        // count.incrementAndGet();
        
        // ✅ 正确写法1：使用 computeIfAbsent + AtomicInteger
        AtomicInteger counter = counters.computeIfAbsent(key, k -> new AtomicInteger(0));
        int newValue = counter.incrementAndGet();
        System.out.println("计数器值: " + newValue);
        
        // ✅ 正确写法2：如果不用 AtomicInteger，用 merge
        ConcurrentHashMap<String, Integer> simpleCounters = new ConcurrentHashMap<>();
        //merge()方法的含义：如果 key 在 simpleCounters 中不存在，则直接插入 (key, 1) 键值对
        //如果 key 已存在，则将现有值与 1 相加（通过 Integer::sum 函数）
        //返回合并后的新值
        int merged = simpleCounters.merge(key, 1, Integer::sum);
        System.out.println("简单计数器值: " + merged);
    }
    
    /**
     * 场景3：对象池/锁池 - 避免重复创建
     * 错误写法：if (!locks.containsKey(id)) { locks.put(id, new Object()); }
     */
    public void demoObjectPool() {
        String userId = "user_123";
        
        // ❌ 错误写法：可能创建多个锁对象
        // if (!locks.containsKey(userId)) {
        //     locks.put(userId, new Object());
        // }
        // Object lock = locks.get(userId);
        
        // ✅ 正确写法：使用 computeIfAbsent 确保每个用户只有一个锁
        Object lock = locks.computeIfAbsent(userId, k -> {
            System.out.println("为用户创建新锁: " + k);
            return new Object();
        });
        
        System.out.println("获取到锁对象: " + lock.hashCode());
        
        // 再次获取，不会创建新锁
        //computeIfAbsent()方法：仅在键不存在时执行: 只有当键在map中不存在或对应值为null时才执行映射函数
        //主要用于添加新键值对，不修改已存在的值
        Object sameLock = locks.computeIfAbsent(userId, k -> new Object());
        System.out.println("再次获取锁对象: " + sameLock.hashCode());
        System.out.println("是否为同一锁: " + (lock == sameLock));
    }
    
    /**
     * 场景4：条件更新 - 避免检查再行动
     * 错误写法：if (cache.get(key) == null) { cache.put(key, value); }
     */
    public void demoConditionalUpdate() {
        String key = "config";
        String newValue = "new_config_value";
        
        // ❌ 错误写法：检查再行动有竞态
        // if (cache.get(key) == null) {
        //     cache.put(key, newValue);
        // }
        
        // ✅ 正确写法1：使用 putIfAbsent
        String previous = cache.putIfAbsent(key, newValue);
        if (previous == null) {
            System.out.println("首次设置配置: " + newValue);
        } else {
            System.out.println("配置已存在: " + previous);
        }
        
        // ✅ 正确写法2：使用 compute 进行条件更新
        String updated = cache.compute(key, (k, v) -> {
            if (v == null || v.length() < 10) {
                return "updated_" + newValue;
            }
            return v; // 保持原值
        });
        System.out.println("条件更新结果: " + updated);
    }
    
    /**
     * 场景5：合并操作 - 避免复杂读改写
     * 错误写法：map.put(key, map.get(key) + delta);
     */
    public void demoMergeOperation() {
        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
        String player = "player1";
        
        // ❌ 错误写法：读改写竞态
        // Integer current = scores.get(player);
        // scores.put(player, (current == null ? 0 : current) + 10);
        
        // ✅ 正确写法：使用 merge
        int newScore = scores.merge(player, 10, Integer::sum);
        System.out.println("玩家分数: " + newScore);
        
        // 再次加分
        newScore = scores.merge(player, 5, Integer::sum);
        System.out.println("加分后分数: " + newScore);
        
        // 使用 compute 进行更复杂的合并逻辑
        //compute方法逻辑：总是执行: 无论键是否存在，都会执行重映射函数
        //可以用于添加新键值对或更新现有键值对
        int finalScore = scores.compute(player, (k, v) -> {
            int current = (v == null) ? 0 : v;
            // 复杂逻辑：分数翻倍，但不超过100
            return Math.min(current * 2, 100);
        });
        System.out.println("翻倍后分数: " + finalScore);
    }
}