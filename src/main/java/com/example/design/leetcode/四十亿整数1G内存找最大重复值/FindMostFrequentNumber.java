package com.example.design.leetcode.四十亿整数1G内存找最大重复值;

import java.io.*;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.nio.file.*;

public class FindMostFrequentNumber {


    private static final String filPath = "E:\\BaiduNetdiskDownload\\左程云算法\\numbers.txt";
    private static final String tempPath = "E:\\BaiduNetdiskDownload\\左程云算法\\temp";

    // 主函数：处理输入文件并找到出现最频繁的数字
    public static long findMostFrequent(String inputFilePath, int numBuckets) throws IOException {
        // 步骤1: 创建分桶文件
        List<Path> bucketFiles = createBucketFiles(inputFilePath, numBuckets);
        
        // 步骤2: 处理每个分桶文件，找到局部最大值
        long globalMaxNumber = -1;
        long globalMaxCount = 0;
        
        for (int i = 0; i < numBuckets; i++) {
            // 处理当前分桶文件
            Map<Long, Long> frequencyMap = new HashMap<>();
            try (BufferedReader reader = Files.newBufferedReader(bucketFiles.get(i))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    long number = Long.parseLong(line.trim());
                    frequencyMap.put(number, frequencyMap.getOrDefault(number, 0L) + 1);
                }
            }
            
            // 在当前分桶中找到出现次数最多的数字
            long localMaxNumber = -1;
            long localMaxCount = 0;
            for (Map.Entry<Long, Long> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > localMaxCount) {
                    localMaxCount = entry.getValue();
                    localMaxNumber = entry.getKey();
                }
            }
            
            // 更新全局最大值
            if (localMaxCount > globalMaxCount) {
                globalMaxCount = localMaxCount;
                globalMaxNumber = localMaxNumber;
            }
            
            // 删除临时文件以节省空间（可选）
            Files.deleteIfExists(bucketFiles.get(i));
        }
        
        return globalMaxNumber;
    }

    // 创建分桶文件并将数字分配到对应的桶中
    private static List<Path> createBucketFiles(String inputFilePath, int numBuckets) throws IOException {
        // 创建临时文件列表
        List<Path> bucketFiles = new ArrayList<>();
        List<BufferedWriter> writers = new ArrayList<>();
        FileAttribute<?>[] attrs = new FileAttribute[0];
        for (int i = 0; i < numBuckets; i++) {
            Path tempFile = Files.createTempFile(Paths.get(tempPath),"bucket_" + i + "_", ".txt");
            bucketFiles.add(tempFile);
            writers.add(Files.newBufferedWriter(tempFile));
        }
        
        // 读取输入文件并分配到桶中
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                long number = Long.parseLong(line.trim());
                
                // 使用哈希函数确定桶索引
                int bucketIndex = hash(number, numBuckets);
                
                // 写入对应的桶文件
                writers.get(bucketIndex).write(String.valueOf(number));
                writers.get(bucketIndex).newLine();
            }
        }
        
        // 关闭所有写入器
        for (BufferedWriter writer : writers) {
            writer.close();
        }
        
        return bucketFiles;
    }

    // 自定义哈希函数（确保均匀分布）
    private static int hash(long number, int numBuckets) {
        // 使用大质数确保均匀分布
        long prime = 15485863; // 100万以内的最大质数
        
        // 哈希计算：确保结果为非负数
        long hash = (number ^ (number >>> 32)) * prime;
        int index = (int) ((hash & 0x7FFFFFFF) % numBuckets); // 确保非负索引
        
        return index;
    }

    public static void main(String[] args) throws IOException {

        //生成测试文件
        //generateTestFile("E:\\BaiduNetdiskDownload\\左程云算法\\numbers.txt", 400000);//40w个整数



        // 参数配置
        String inputFile = filPath; // 输入文件路径
        int numBuckets = 400;             // 分桶数量（基于内存估算）

        try {
            long startTime = System.currentTimeMillis();

            // 执行查找
            long mostFrequent = findMostFrequent(inputFile, numBuckets);

            long endTime = System.currentTimeMillis();

            System.out.println("Most frequent number: " + mostFrequent);
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    // 辅助方法：生成测试文件（仅用于演示）
    public static void generateTestFile(String filename, long numEntries) throws IOException {
        Random rand = new Random();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            for (long i = 0; i < numEntries; i++) {
                // 生成测试数据（70%是重复数字）
                long number = rand.nextInt(100) < 70 ? 42 : rand.nextInt(1000000);
                writer.write(String.valueOf(number));
                writer.newLine();
            }
        }
    }
}