package com.example.design.mapTest.hashmap内存使用分析;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * HashMap内存分析工具
 * 使用JOL工具精确分析HashMap的内存占用
 */
public class HashMapMemoryAnalysis {
    
    public static void main(String[] args) {
        System.out.println("=== HashMap内存分析演示 ===\n");
        
        // 1. 分析HashMap.Node类的内存布局
        analyzeHashMapNodeLayout();
        
        // 2. 分析空HashMap的内存占用
        analyzeEmptyHashMap();
        
        // 3. 分析包含数据的HashMap
        analyzeHashMapWithData();
        
        // 4. 分析不同容量HashMap的内存差异
        analyzeHashMapCapacity();
        
        // 5. 精确计算单个键值对的内存开销
        calculateSingleEntryCost();
    }
    
    /**
     * 分析HashMap.Node类的内存布局
     */
    private static void analyzeHashMapNodeLayout() {
        System.out.println("1. HashMap.Node类内存布局分析:");
        
        // 分析HashMap.Node类的内存布局
        System.out.println(ClassLayout.parseClass(HashMap.class).toPrintable());
    }
    
    /**
     * 分析空HashMap的内存占用
     */
    private static void analyzeEmptyHashMap() {
        System.out.println("\n2. 空HashMap内存占用分析:");
        
        HashMap<Integer, Integer> emptyMap = new HashMap<>();
        
        // 使用GraphLayout分析整个对象图
        System.out.println("空HashMap总内存占用:");
        System.out.println(GraphLayout.parseInstance(emptyMap).toFootprint());
        
        // 详细布局分析
        System.out.println("\n空HashMap详细内存布局:");
        System.out.println(ClassLayout.parseInstance(emptyMap).toPrintable());
    }
    
    /**
     * 分析包含数据的HashMap
     */
    private static void analyzeHashMapWithData() {
        System.out.println("\n3. 包含数据的HashMap内存分析:");
        
        HashMap<Integer, Integer> map = new HashMap<>();
        
        // 添加一些数据
        for (int i = 0; i < 10; i++) {
            map.put(i, i * 10);
        }
        
        System.out.println("包含10个元素的HashMap内存占用:");
        System.out.println(GraphLayout.parseInstance(map).toFootprint());
        
        // 分析单个Node对象
        System.out.println("\n单个HashMap.Node对象内存布局:");
        Map.Entry<Integer, Integer> entry = map.entrySet().iterator().next();
        System.out.println(ClassLayout.parseInstance(entry).toPrintable());
        
        // 分析Integer对象
        System.out.println("\nInteger对象内存布局:");
        Integer integer = 42;
        System.out.println(ClassLayout.parseInstance(integer).toPrintable());
    }
    
    /**
     * 分析不同容量HashMap的内存差异
     */
    private static void analyzeHashMapCapacity() {
        System.out.println("\n4. 不同容量HashMap内存对比:");
        
        // 默认容量16
        HashMap<Integer, Integer> map16 = new HashMap<>(16);
        for (int i = 0; i < 8; i++) {
            map16.put(i, i);
        }
        
        // 较大容量
        HashMap<Integer, Integer> map64 = new HashMap<>(64);
        for (int i = 0; i < 8; i++) {
            map64.put(i, i);
        }
        
        System.out.println("容量16的HashMap内存占用:");
        System.out.println(GraphLayout.parseInstance(map16).totalSize() + " bytes");
        
        System.out.println("容量64的HashMap内存占用:");
        System.out.println(GraphLayout.parseInstance(map64).totalSize() + " bytes");
        
        // 分析负载因子影响
        System.out.println("\n负载因子对内存的影响:");
        HashMap<Integer, Integer> mapLowLoad = new HashMap<>(16, 0.25f);
        HashMap<Integer, Integer> mapHighLoad = new HashMap<>(16, 0.75f);
        
        for (int i = 0; i < 8; i++) {
            mapLowLoad.put(i, i);
            mapHighLoad.put(i, i);
        }
        
        System.out.println("负载因子0.25: " + GraphLayout.parseInstance(mapLowLoad).totalSize() + " bytes");
        System.out.println("负载因子0.75: " + GraphLayout.parseInstance(mapHighLoad).totalSize() + " bytes");
    }
    
    /**
     * 精确计算单个键值对的内存开销
     */
    private static void calculateSingleEntryCost() {
        System.out.println("\n5. 单个键值对内存开销精确计算:");
        
        // 创建一个包含单个元素的HashMap
        HashMap<Integer, Integer> singleMap = new HashMap<>();
        singleMap.put(1, 100);
        
        long singleMapSize = GraphLayout.parseInstance(singleMap).totalSize();
        
        // 创建空HashMap
        HashMap<Integer, Integer> emptyMap = new HashMap<>();
        long emptyMapSize = GraphLayout.parseInstance(emptyMap).totalSize();
        
        // 计算单个键值对的开销
        long entryCost = singleMapSize - emptyMapSize;
        
        System.out.println("空HashMap大小: " + emptyMapSize + " bytes");
        System.out.println("单元素HashMap大小: " + singleMapSize + " bytes");
        System.out.println("单个键值对开销: " + entryCost + " bytes");
        
        // 分析开销组成
        System.out.println("\n开销组成分析:");
        Integer key = 1;
        Integer value = 100;
        Map.Entry<Integer, Integer> entry = singleMap.entrySet().iterator().next();
        
        System.out.println("Integer key大小: " + GraphLayout.parseInstance(key).totalSize() + " bytes");
        System.out.println("Integer value大小: " + GraphLayout.parseInstance(value).totalSize() + " bytes");
        System.out.println("HashMap.Node大小: " + GraphLayout.parseInstance(entry).totalSize() + " bytes");
    }
}