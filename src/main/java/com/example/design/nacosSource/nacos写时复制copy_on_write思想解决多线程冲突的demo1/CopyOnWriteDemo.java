package com.example.design.nacosSource.nacos写时复制copy_on_write思想解决多线程冲突的demo1;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用写时复制(Copy-On-Write)解决并发冲突的示例
 * 模拟服务注册中心的实例管理
 */
public class CopyOnWriteDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建服务集群
        ServiceCluster cluster = new ServiceCluster("order-service");
        
        // 创建读写线程池
        ExecutorService executor = Executors.newFixedThreadPool(8);
        AtomicInteger updateCounter = new AtomicInteger();
        
        // 启动3个写线程（模拟服务注册/注销）
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                while (true) {
                    try {
                        // 随机更新实例
                        List<ServiceInstance> newInstances = generateInstances();
                        cluster.updateInstances(newInstances);
                        
                        int count = updateCounter.incrementAndGet();
                        if (count % 10 == 0) {
                            System.out.printf("[Writer-%d] 更新 %d 次, 当前实例数: %d%n",
                                    Thread.currentThread().getId(), count, cluster.getInstanceCount());
                        }
                        
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 启动5个读线程（模拟服务发现）
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                while (true) {
                    try {
                        // 读取所有实例
                        List<ServiceInstance> instances = cluster.getAllInstances();
                        
                        // 验证读取一致性
                        if (!instances.isEmpty()) {
                            ServiceInstance first = instances.get(0);
                            for (ServiceInstance instance : instances) {
                                if (!instance.serviceName.equals(first.serviceName)) {
                                    System.err.println("!!! 数据损坏检测: " + instance);
                                }
                            }
                        }
                        
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(30));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 运行10秒后关闭
        TimeUnit.SECONDS.sleep(10);
        executor.shutdownNow();
        System.out.println("最终实例数量: " + cluster.getInstanceCount());
    }
    
    // 随机生成实例列表
    private static List<ServiceInstance> generateInstances() {
        List<ServiceInstance> instances = new ArrayList<>();
        int count = ThreadLocalRandom.current().nextInt(1, 6);
        
        for (int i = 0; i < count; i++) {
            String ip = "192.168." + ThreadLocalRandom.current().nextInt(1, 255) + 
                      "." + ThreadLocalRandom.current().nextInt(1, 255);
            int port = ThreadLocalRandom.current().nextInt(8080, 8100);
            instances.add(new ServiceInstance("order-service", ip, port));
        }
        return instances;
    }
    
    /**
     * 服务实例类
     */
    static class ServiceInstance {
        final String serviceName;
        final String ip;
        final int port;
        
        public ServiceInstance(String serviceName, String ip, int port) {
            this.serviceName = serviceName;
            this.ip = ip;
            this.port = port;
        }
        
        @Override
        public String toString() {
            return serviceName + "@" + ip + ":" + port;
        }
        
        // 重写equals和hashCode用于去重
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ServiceInstance that = (ServiceInstance) o;
            return port == that.port && ip.equals(that.ip);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(ip, port);
        }
    }
    
    /**
     * 服务集群管理（核心Copy-On-Write实现）
     */
    static class ServiceCluster {
        private final String clusterName;
        
        // volatile保证引用的可见性
        private volatile Set<ServiceInstance> instances = Collections.emptySet();
        
        public ServiceCluster(String clusterName) {
            this.clusterName = clusterName;
        }
        
        /**
         * 更新实例列表（写时复制核心方法）
         * // 整个过程在单个写线程中执行
         * @param newInstances 新实例列表
         */
        public void updateInstances(List<ServiceInstance> newInstances) {
            // 1. 创建当前实例的快照（读操作不受影响）
            Set<ServiceInstance> currentInstances = this.instances;
            
            // 2. 创建新实例集合（复制过程）
            Set<ServiceInstance> updatedSet = new HashSet<>(newInstances);
            
            // 3. 执行数据转移（如状态继承）
            transferState(currentInstances, updatedSet);//这些集合引用虽然指向共享数据，但方法内使用的只是引用的副本
            
            // 4. 原子替换引用（写操作唯一同步点）
            this.instances = Collections.unmodifiableSet(updatedSet);
        }
        
        // 转移实例状态（如健康状态）
        private void transferState(Set<ServiceInstance> oldSet, Set<ServiceInstance> newSet) {//整个transferState()方法在写线程中执行调用栈：写线程 → updateInstances() → transferState()没有其他线程能访问这个方法内的局部变量
            Map<String, ServiceInstance> oldMap = new HashMap<>();//局部变量，在写线程里的，不存在并发读写冲突
            for (ServiceInstance instance : oldSet) {
                oldMap.put(instance.ip + ":" + instance.port, instance);
            }
            
            // 实际应用中这里可以转移健康状态等属性
            // 这里简化为打印变更日志
            //比较操作（仅写线程访问）
            for (ServiceInstance instance : newSet) {
                String key = instance.ip + ":" + instance.port;
                if (!oldMap.containsKey(key)) {
                    System.out.println("[状态转移] 新增实例: " + instance);
                }
            }
            
            for (ServiceInstance instance : oldSet) {
                String key = instance.ip + ":" + instance.port;
                if (!newSet.contains(instance)) {
                    System.out.println("[状态转移] 移除实例: " + instance);
                }
            }
        }
        
        /**
         * 获取所有实例（无锁读取）
         */
        public List<ServiceInstance> getAllInstances() {
            // 直接返回当前不可变集合（读操作线程安全）
            return new ArrayList<>(instances);
        }
        
        public int getInstanceCount() {
            return instances.size();
        }
    }
}