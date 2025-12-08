package com.example.design.consistentHash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 一致性哈希路由算法Demo
 * 
 * 这是一个基于虚拟节点的一致性哈希实现示例，用于学习一致性哈希算法的核心原理
 * 和实际应用场景。
 * 
 * 核心特性：
 * 1. 使用虚拟节点（160个）实现负载均衡
 * 2. 支持集群的动态添加和移除
 * 3. 基于MD5哈希算法构建哈希环
 * 4. 提供平滑的数据迁移机制
 * 
 * @author RuYuan MQ Team
 */
public class ConsistentHashDemo {
    
    // 虚拟节点数量，每个集群对应160个虚拟节点
    private static final int VIRTUAL_NODES_PER_CLUSTER = 160;
    
    // 哈希环，使用TreeMap实现有序哈希环
    private final SortedMap<Long, ClusterInfo> hashRing = new TreeMap<>();
    
    // 集群映射，用于维护集群信息
    private final Map<String, ClusterInfo> clusterMap = new HashMap<>();
    
    /**
     * 集群信息类
     */
    public static class ClusterInfo {
        private String clusterId;
        private String clusterName;
        private String clusterType;
        private String region;
        private boolean healthy;
        
        public ClusterInfo(String clusterId, String clusterName, String clusterType, String region) {
            this.clusterId = clusterId;
            this.clusterName = clusterName;
            this.clusterType = clusterType;
            this.region = region;
            this.healthy = true;
        }
        
        // Getters and Setters
        public String getClusterId() { return clusterId; }
        public String getClusterName() { return clusterName; }
        public String getClusterType() { return clusterType; }
        public String getRegion() { return region; }
        public boolean isHealthy() { return healthy; }
        public void setHealthy(boolean healthy) { this.healthy = healthy; }
        
        @Override
        public String toString() {
            return String.format("Cluster{id='%s', name='%s', type='%s', region='%s', healthy=%s}",
                    clusterId, clusterName, clusterType, region, healthy);
        }
    }
    
    /**
     * 可路由消息类
     */
    public static class RoutableMessage {
        private String topic;
        private String tags;
        private String keys;
        private String messageType;
        private boolean ordered;
        private boolean transaction;
        
        public RoutableMessage(String topic, String tags, String keys) {
            this.topic = topic;
            this.tags = tags;
            this.keys = keys;
        }
        
        // Getters and Setters
        public String getTopic() { return topic; }
        public String getTags() { return tags; }
        public String getKeys() { return keys; }
        public String getMessageType() { return messageType; }
        public boolean isOrdered() { return ordered; }
        public boolean isTransaction() { return transaction; }
        
        public void setMessageType(String messageType) { this.messageType = messageType; }
        public void setOrdered(boolean ordered) { this.ordered = ordered; }
        public void setTransaction(boolean transaction) { this.transaction = transaction; }
        
        @Override
        public String toString() {
            return String.format("Message{topic='%s', tags='%s', keys='%s', type='%s', ordered=%s, transaction=%s}",
                    topic, tags, keys, messageType, ordered, transaction);
        }
    }
    
    /**
     * 添加集群到哈希环
     */
    public void addCluster(ClusterInfo cluster) {
        if (clusterMap.containsKey(cluster.getClusterId())) {
            System.out.println("集群已存在: " + cluster.getClusterId());
            return;
        }
        
        clusterMap.put(cluster.getClusterId(), cluster);
        
        // 为集群创建虚拟节点
        for (int i = 0; i < VIRTUAL_NODES_PER_CLUSTER; i++) {
            String virtualNodeName = cluster.getClusterId() + "#VN" + i;
            long hash = hash(virtualNodeName);
            hashRing.put(hash, cluster);
        }
        
        System.out.println("添加集群成功: " + cluster + ", 虚拟节点数: " + VIRTUAL_NODES_PER_CLUSTER);
    }
    
    /**
     * 从哈希环中移除集群
     */
    public void removeCluster(String clusterId) {
        ClusterInfo cluster = clusterMap.remove(clusterId);
        if (cluster == null) {
            System.out.println("集群不存在: " + clusterId);
            return;
        }
        
        // 移除该集群的所有虚拟节点
        hashRing.entrySet().removeIf(entry -> entry.getValue().getClusterId().equals(clusterId));
        
        System.out.println("移除集群成功: " + clusterId);
    }
    
    /**
     * 为消息选择目标集群
     */
    public ClusterInfo selectCluster(RoutableMessage message) {
        if (hashRing.isEmpty()) {
            System.out.println("哈希环为空，无法选择集群");
            return null;
        }
        
        // 构建路由键
        String routingKey = buildRoutingKey(message);
        long hash = hash(routingKey);
        
        // 从哈希环中选择集群
        ClusterInfo selectedCluster = getClusterFromHashRing(hash);
        
        System.out.println(String.format("消息路由: %s -> 集群: %s (哈希: %d)", 
                routingKey, selectedCluster.getClusterId(), hash));
        
        return selectedCluster;
    }
    
    /**
     * 从哈希环中获取集群
     */
    private ClusterInfo getClusterFromHashRing(long hash) {
        if (hashRing.isEmpty()) {
            return null;
        }
        
        // 查找第一个大于等于hash值的节点
        SortedMap<Long, ClusterInfo> tailMap = hashRing.tailMap(hash);
        
        // 如果没有找到，则选择第一个节点（环形结构）
        Long targetHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
        
        return hashRing.get(targetHash);
    }
    
    /**
     * 构建路由键
     */
    private String buildRoutingKey(RoutableMessage message) {
        StringBuilder routingKey = new StringBuilder();
        
        // 基础路由键：topic + tags + keys
        routingKey.append(message.getTopic()).append("#")
                  .append(message.getTags()).append("#")
                  .append(message.getKeys());
        
        // 根据消息特性调整路由键
        if (message.isOrdered()) {
            routingKey.append("#ORDERED");
        }
        if (message.isTransaction()) {
            routingKey.append("#TRANSACTION");
        }
        if (message.getMessageType() != null) {
            routingKey.append("#").append(message.getMessageType());
        }
        
        return routingKey.toString();
    }

    /**
     * 重建哈希环，这是全量重建
     * 这种方法的问题在于：
     * - 性能问题 ：每次添加/移除集群都要重新计算所有虚拟节点的哈希值
     * - 路由不稳定 ：同一消息在不同时间可能路由到不同集群
     * - 丧失一致性哈希优势 ：无法实现平滑的数据迁移
     */
    private void rebuildHashRing(List<ClusterInfo> clusters) {
        hashRing.clear();

        for (ClusterInfo cluster : clusters) {
            if (!cluster.isHealthy()) {
                continue;
            }

            // 为每个集群创建虚拟节点
            for (int i = 0; i < VIRTUAL_NODES_PER_CLUSTER; i++) {
                String virtualNodeName = cluster.getClusterId() + "#VN" + i;
                long hash = hash(virtualNodeName);
                hashRing.put(hash, cluster);
            }
        }

        System.out.println("重建哈希环完成，节点数量: "+hashRing.size());
    }
    
    /**
     * 计算哈希值（使用MD5算法）
     */
    private long hash(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            byte[] digest = md5.digest();
            
            // 取前8个字节转换为long值
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }
            
            return hash;
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，使用简单的哈希算法
            return key.hashCode() & 0xFFFFFFFFL;
        }
    }
    
    /**
     * 获取哈希环信息
     */
    public void printHashRingInfo() {
        System.out.println("=== 哈希环信息 ===");
        System.out.println("集群数量: " + clusterMap.size());
        System.out.println("虚拟节点总数: " + hashRing.size());
        System.out.println("哈希环范围: [" + hashRing.firstKey() + " -> " + hashRing.lastKey() + "]");
        
        // 统计每个集群的虚拟节点分布
        Map<String, Integer> clusterNodeCount = new HashMap<>();
        for (ClusterInfo cluster : hashRing.values()) {
            String clusterId = cluster.getClusterId();
            clusterNodeCount.put(clusterId, clusterNodeCount.getOrDefault(clusterId, 0) + 1);
        }
        
        System.out.println("集群虚拟节点分布:");
        for (Map.Entry<String, Integer> entry : clusterNodeCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " 个虚拟节点");
        }
        System.out.println();
    }
    
    /**
     * 测试方法：演示一致性哈希路由
     */
    public static void main(String[] args) {
        ConsistentHashDemo router = new ConsistentHashDemo();
        
        // 1. 初始化集群
        System.out.println("=== 初始化集群 ===");
        ClusterInfo cluster1 = new ClusterInfo("order-cluster", "订单集群", "ORDER", "beijing");
        ClusterInfo cluster2 = new ClusterInfo("log-cluster", "日志集群", "LOG", "shanghai");
        ClusterInfo cluster3 = new ClusterInfo("cache-cluster", "缓存集群", "CACHE", "guangzhou");
        
//        router.addCluster(cluster1);
//        router.addCluster(cluster2);
//        router.addCluster(cluster3);
        //注释掉一个一个add，使用重建集群方法
        router.rebuildHashRing(Arrays.asList(cluster1, cluster2, cluster3));
        
        router.printHashRingInfo();
        
        // 2. 测试消息路由
        System.out.println("=== 测试消息路由 ===");
        
        // 订单消息
        RoutableMessage orderMessage = new RoutableMessage("order.payment", "pay", "order-001");
        orderMessage.setMessageType("order");
        orderMessage.setOrdered(true);
        
        // 日志消息
        RoutableMessage logMessage = new RoutableMessage("log.audit", "audit", "audit-001");
        logMessage.setMessageType("log");
        
        // 缓存消息
        RoutableMessage cacheMessage = new RoutableMessage("cache.update", "update", "cache-001");
        cacheMessage.setMessageType("cache");
        
        // 路由测试
        ClusterInfo orderCluster = router.selectCluster(orderMessage);
        ClusterInfo logCluster = router.selectCluster(logMessage);
        ClusterInfo cacheCluster = router.selectCluster(cacheMessage);
        
        System.out.println();
        
        // 3. 演示集群扩展
        System.out.println("=== 演示集群扩展 ===");
        ClusterInfo newCluster = new ClusterInfo("new-cluster", "新集群", "GENERAL", "shenzhen");
        router.addCluster(newCluster);
        
        router.printHashRingInfo();
        
        // 测试扩展后的路由
        System.out.println("=== 扩展后路由测试 ===");
        ClusterInfo orderClusterAfter = router.selectCluster(orderMessage);
        ClusterInfo logClusterAfter = router.selectCluster(logMessage);
        ClusterInfo cacheClusterAfter = router.selectCluster(cacheMessage);
        
        // 4. 演示集群移除
        System.out.println("=== 演示集群移除 ===");
        router.removeCluster("cache-cluster");
        
        router.printHashRingInfo();
        
        // 测试移除后的路由
        System.out.println("=== 移除后路由测试 ===");
        ClusterInfo orderClusterFinal = router.selectCluster(orderMessage);
        ClusterInfo logClusterFinal = router.selectCluster(logMessage);
        
        // 5. 路由一致性验证
        System.out.println("=== 路由一致性验证 ===");
        System.out.println("订单消息路由一致性: " + 
                (orderCluster.getClusterId().equals(orderClusterAfter.getClusterId()) &&
                 orderClusterAfter.getClusterId().equals(orderClusterFinal.getClusterId()) ? "一致" : "不一致"));
        System.out.println("日志消息路由一致性: " + 
                (logCluster.getClusterId().equals(logClusterAfter.getClusterId()) &&
                 logClusterAfter.getClusterId().equals(logClusterFinal.getClusterId()) ? "一致" : "不一致"));
    }
}