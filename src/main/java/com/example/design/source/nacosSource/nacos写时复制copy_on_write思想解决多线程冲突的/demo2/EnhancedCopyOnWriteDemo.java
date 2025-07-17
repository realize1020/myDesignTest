package com.example.design.source.nacosSource.nacos写时复制copy_on_write思想解决多线程冲突的.demo2;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EnhancedCopyOnWriteDemo {
    public static void main(String[] args) throws InterruptedException {
        EnhancedServiceRegistry registry = new EnhancedServiceRegistry();
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger operationCounter = new AtomicInteger();
        
        // 启动3个写线程
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String serviceName = "service-" + ThreadLocalRandom.current().nextInt(1, 4);
                        List<ServiceInstance> newInstances = generateInstances(serviceName);
                        
                        registry.updateService(serviceName, newInstances);
                        
                        int count = operationCounter.incrementAndGet();
                        if (count % 10 == 0) {
                            System.out.printf("[Writer-%d] 更新服务: %s, 实例数: %d, 版本: %d%n",
                                    Thread.currentThread().getId(), serviceName, 
                                    newInstances.size(), registry.getServiceVersion(serviceName));
                        }
                        
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 启动5个读线程
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String serviceName = "service-" + ThreadLocalRandom.current().nextInt(1, 4);
                        
                        // 普通读取
                        List<ServiceInstance> instances = registry.getInstances(serviceName);
                        
                        // 带版本号的读取
                        /*long version = registry.getServiceVersion(serviceName);
                        List<ServiceInstance> versionedInstances = registry.getInstances(serviceName, version);*/
                        
                        // 强一致性读取
                        //List<ServiceInstance> consistentInstances = registry.getConsistentInstances(serviceName);
                        
                        // 验证读取的数据一致性
                        //validateConsistency(serviceName, instances, versionedInstances, consistentInstances);
                        System.out.printf("[Reader-%d] 读服务: %s, 实例数: %d, 版本: %d%n",
                                Thread.currentThread().getId(), serviceName,
                                instances.size(), registry.getServiceVersion(serviceName));
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(30));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 运行15秒后关闭
        TimeUnit.SECONDS.sleep(15);
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        System.out.println("\n最终统计:");
        registry.printRegistryStats();
    }
    
    // 数据一致性验证
    private static void validateConsistency(String serviceName, 
                                           List<ServiceInstance> normal,
                                           List<ServiceInstance> versioned,
                                           List<ServiceInstance> consistent) {
        // 普通读取和强一致性读取应该一致
        if (!normal.equals(consistent)) {
            System.err.printf("!!! 数据不一致检测 [%s]:\n  普通读取: %d\n  强一致读取: %d%n", 
                    serviceName, normal.size(), consistent.size());
        }
        
        // 带版本号的读取应该与请求版本一致
        long version = versioned.get(0).version; // 所有实例版本相同
        if (versioned.size() != consistent.size() && !versioned.equals(normal)) {
            System.out.printf("[%s] 版本读取: v%d (%d实例) vs 当前版本: v%d (%d实例)%n",
                    serviceName, version, versioned.size(), 
                    consistent.get(0).version, consistent.size());
        }
    }
    
    // 生成随机服务实例
    private static List<ServiceInstance> generateInstances(String serviceName) {
        List<ServiceInstance> instances = new ArrayList<>();
        int count = ThreadLocalRandom.current().nextInt(1, 6);
        
        for (int i = 0; i < count; i++) {
            String ip = "192.168." + ThreadLocalRandom.current().nextInt(1, 255) + 
                      "." + ThreadLocalRandom.current().nextInt(1, 255);
            int port = ThreadLocalRandom.current().nextInt(8080, 8100);
            instances.add(new ServiceInstance(serviceName, ip, port));
        }
        return instances;
    }
    
    // 服务实例类（带版本号）
    static class ServiceInstance {
        final String serviceName;
        final String ip;
        final int port;
        final long version; // 实例所属的服务版本
        
        public ServiceInstance(String serviceName, String ip, int port) {
            this(serviceName, ip, port, -1);
        }
        
        public ServiceInstance(String serviceName, String ip, int port, long version) {
            this.serviceName = serviceName;
            this.ip = ip;
            this.port = port;
            this.version = version;
        }
        
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
        
        @Override
        public String toString() {
            return serviceName + "@" + ip + ":" + port + " [v" + version + "]";
        }
    }
    
    /**
     * 增强版服务注册中心 - 解决写时复制数据不一致问题
     */
    static class EnhancedServiceRegistry {
        // 服务注册表 <服务名, 服务状态>
        private final Map<String, ServiceState> registry = new ConcurrentHashMap<>();
        private final ReadWriteLock versionLock = new ReentrantReadWriteLock();
        
        // 服务状态（包含实例和版本信息）
        private static class ServiceState {
            private volatile long version = 0;
            private volatile Set<ServiceInstance> instances = Collections.emptySet();
            private final Lock updateLock = new ReentrantLock();
            
            // 获取当前版本
            public long getVersion() {
                return version;
            }
            
            // 获取当前实例
            public Set<ServiceInstance> getInstances() {
                return instances;
            }
            
            // 带版本号的读取
            public Set<ServiceInstance> getInstances(long version) {
                if (this.version == version) {
                    return instances;
                }
                return null; // 版本不匹配返回null
            }
            
            // 更新实例
            public void updateInstances(Set<ServiceInstance> newInstances) {
                updateLock.lock();
                try {
                    // 1. 创建新版本实例（继承版本号）
                    Set<ServiceInstance> versionedInstances = new HashSet<>();
                    for (ServiceInstance instance : newInstances) {
                        versionedInstances.add(
                            new ServiceInstance(instance.serviceName, instance.ip, instance.port, version + 1)
                        );
                    }
                    
                    // 2. 更新引用
                    instances = Collections.unmodifiableSet(versionedInstances);
                    
                    // 3. 增加版本号（必须在更新实例后）
                    version++;
                } finally {
                    updateLock.unlock();
                }
            }
        }
        
        // 更新服务实例
        public void updateService(String serviceName, List<ServiceInstance> newInstances) {
            ServiceState state = registry.computeIfAbsent(serviceName, k -> new ServiceState());
            state.updateInstances(new HashSet<>(newInstances));
        }
        
        // 获取服务实例（普通读取）
        public List<ServiceInstance> getInstances(String serviceName) {
            ServiceState state = registry.get(serviceName);
            if (state == null) return Collections.emptyList();
            return new ArrayList<>(state.getInstances());
        }
        
        // 获取服务实例（带版本号读取）
        public List<ServiceInstance> getInstances(String serviceName, long version) {
            ServiceState state = registry.get(serviceName);
            if (state == null) return Collections.emptyList();
            
            Set<ServiceInstance> instances = state.getInstances(version);
            if (instances != null) {
                return new ArrayList<>(instances);
            }
            
            // 版本不匹配时返回最新数据
            return new ArrayList<>(state.getInstances());
        }
        
        // 获取服务实例（强一致性读取）
        public List<ServiceInstance> getConsistentInstances(String serviceName) {
            ServiceState state = registry.get(serviceName);
            if (state == null) return Collections.emptyList();
            
            // 使用读锁确保读取期间不会发生版本变化
            versionLock.readLock().lock();
            try {
                long version = state.getVersion();
                List<ServiceInstance> result = new ArrayList<>(state.getInstances());
                
                // 双重检查版本是否变化
                if (version == state.getVersion()) {
                    return result;
                }
                
                // 如果版本变化，重新获取
                return new ArrayList<>(state.getInstances());
            } finally {
                versionLock.readLock().unlock();
            }
        }
        
        // 获取服务版本
        public long getServiceVersion(String serviceName) {
            ServiceState state = registry.get(serviceName);
            return state != null ? state.getVersion() : -1;
        }
        
        // 打印注册中心统计信息
        public void printRegistryStats() {
            System.out.println("服务注册中心统计:");
            registry.forEach((name, state) -> {
                System.out.printf("  服务: %-12s  版本: %-4d  实例数: %d%n", 
                        name, state.getVersion(), state.getInstances().size());
            });
        }
    }
}