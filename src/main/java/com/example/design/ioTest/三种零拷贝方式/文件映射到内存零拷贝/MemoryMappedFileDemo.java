package com.example.design.ioTest.三种零拷贝方式.文件映射到内存零拷贝;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 文件映射零拷贝Demo
 * 演示使用MappedByteBuffer实现高性能零拷贝文件操作
 */
public class MemoryMappedFileDemo {
    
    private static final int FILE_SIZE = 1024 * 1024; // 1MB文件大小
    private static final int PAGE_SIZE = 4096; // 4KB页面大小
    
    public static void main(String[] args) {
        String fileName = "memory_mapped_demo.dat";
        
        try {
            // 1. 基础内存映射操作演示
            System.out.println("=== 1. 基础内存映射操作演示 ===");
            basicMemoryMappingDemo(fileName);
            
            // 2. 高性能写入演示
            System.out.println("\n=== 2. 高性能写入演示 ===");
            highPerformanceWriteDemo(fileName);
            
            // 3. 零拷贝读取演示
            System.out.println("\n=== 3. 零拷贝读取演示 ===");
            zeroCopyReadDemo(fileName);
            
            // 4. 并发访问演示
            System.out.println("\n=== 4. 并发访问演示 ===");
            concurrentAccessDemo(fileName);
            
            // 5. 大文件分块映射演示
            System.out.println("\n=== 5. 大文件分块映射演示 ===");
            largeFileMappingDemo(fileName + "_large", 10 * 1024 * 1024); // 10MB大文件
            
            System.out.println("\n=== 所有演示完成 ===");
            
        } catch (Exception e) {
            System.err.println("演示失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理演示文件
            new File(fileName).delete();
            new File(fileName + "_large").delete();
        }
    }
    
    /**
     * 基础内存映射操作演示
     */
    private static void basicMemoryMappingDemo(String fileName) throws IOException {
        System.out.println("创建内存映射文件: " + fileName);
        
        // 创建RandomAccessFile和FileChannel
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = raf.getChannel();
        
        // 预分配文件空间
        raf.setLength(FILE_SIZE);
        System.out.println("文件空间预分配完成: " + FILE_SIZE + " bytes");
        
        // 创建内存映射缓冲区
        MappedByteBuffer mappedBuffer = fileChannel.map(
            FileChannel.MapMode.READ_WRITE, 0, FILE_SIZE);
        System.out.println("内存映射创建成功，缓冲区容量: " + mappedBuffer.capacity());
        
        // 写入数据到内存映射缓冲区
        System.out.println("\n--- 写入数据到内存映射缓冲区 ---");
        String message = "Hello, Memory Mapped File! 这是内存映射文件测试数据。";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        
        // 在文件开头写入消息长度和内容
        mappedBuffer.putInt(messageBytes.length); // 写入消息长度
        mappedBuffer.put(messageBytes);           // 写入消息内容
        
        // 在文件中间位置写入另一条消息
        int middlePosition = FILE_SIZE / 2;
        mappedBuffer.position(middlePosition);
        String middleMessage = "这是文件中间位置的数据";
        byte[] middleBytes = middleMessage.getBytes(StandardCharsets.UTF_8);
        mappedBuffer.putInt(middleBytes.length);
        mappedBuffer.put(middleBytes);
        
        // 强制刷盘到磁盘
        mappedBuffer.force();
        System.out.println("数据写入完成并强制刷盘");
        
        // 读取数据演示
        System.out.println("\n--- 从内存映射缓冲区读取数据 ---");
        
        // 读取文件开头的消息
        mappedBuffer.position(0);
        int messageLength = mappedBuffer.getInt();
        byte[] readBytes = new byte[messageLength];
        mappedBuffer.get(readBytes);
        String readMessage = new String(readBytes, StandardCharsets.UTF_8);
        System.out.println("读取文件开头消息: " + readMessage);
        
        // 读取文件中间的消息
        mappedBuffer.position(middlePosition);
        int middleLength = mappedBuffer.getInt();
        byte[] middleReadBytes = new byte[middleLength];
        mappedBuffer.get(middleReadBytes);
        String middleReadMessage = new String(middleReadBytes, StandardCharsets.UTF_8);
        System.out.println("读取文件中间消息: " + middleReadMessage);
        
        // 关闭资源
        fileChannel.close();
        raf.close();
        System.out.println("基础演示完成");
    }
    
    /**
     * 高性能写入演示
     */
    private static void highPerformanceWriteDemo(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = raf.getChannel();
        MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, FILE_SIZE);
        
        System.out.println("开始高性能写入测试...");
        
        // 测试写入性能
        int testSize = 1024 * 100; // 100KB测试数据
        byte[] testData = generateTestData(testSize);
        
        long startTime = System.nanoTime();
        
        // 使用内存映射写入
        mappedBuffer.position(100); // 从100字节位置开始写入
        for (int i = 0; i < 1000; i++) {
            mappedBuffer.put(testData, 0, Math.min(100, testData.length));
        }
        mappedBuffer.force(); // 一次性刷盘
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("高性能写入完成:");
        System.out.println("写入数据量: " + (1000 * 100) + " bytes");
        System.out.println("耗时: " + TimeUnit.NANOSECONDS.toMicros(duration) + " μs");
        System.out.println("吞吐量: " + 
            String.format("%.2f", (1000 * 100 * 1000000.0 / duration)) + " bytes/s");
        
        fileChannel.close();
        raf.close();
    }
    
    /**
     * 零拷贝读取演示
     */
    private static void zeroCopyReadDemo(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        FileChannel fileChannel = raf.getChannel();
        MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);
        
        System.out.println("开始零拷贝读取演示...");
        
        // 直接操作内存映射缓冲区，避免数据拷贝
        long startTime = System.nanoTime();
        
        // 模拟零拷贝处理：直接在内存中处理数据
        int processedBytes = processDataDirectly(mappedBuffer);
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("零拷贝读取完成:");
        System.out.println("处理数据量: " + processedBytes + " bytes");
        System.out.println("耗时: " + TimeUnit.NANOSECONDS.toMicros(duration) + " μs");
        System.out.println("零拷贝优势: 避免了" + processedBytes + "字节的数据拷贝");
        
        fileChannel.close();
        raf.close();
    }
    
    /**
     * 并发访问演示
     */
    private static void concurrentAccessDemo(String fileName) throws IOException, InterruptedException {
        System.out.println("创建并发访问测试文件...");
        
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = raf.getChannel();
        raf.setLength(FILE_SIZE);
        
        final MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, FILE_SIZE);
        
        int threadCount = 4;
        int writesPerThread = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        System.out.println("启动" + threadCount + "个线程进行并发写入...");
        long startTime = System.nanoTime();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // 每个线程写入不同的文件区域
                    int startPos = threadId * (FILE_SIZE / threadCount);
                    byte[] threadData = ("Thread-" + threadId + " data").getBytes(StandardCharsets.UTF_8);
                    
                    for (int j = 0; j < writesPerThread; j++) {
                        int writePos = startPos + j * 100;
                        if (writePos + threadData.length < FILE_SIZE) {
                            synchronized (mappedBuffer) {
                                mappedBuffer.position(writePos);
                                mappedBuffer.put(threadData);
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        mappedBuffer.force(); // 所有线程完成后一次性刷盘
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("并发写入完成:");
        System.out.println("总写入次数: " + (threadCount * writesPerThread));
        System.out.println("总耗时: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");
        System.out.println("平均每次写入耗时: " + 
            String.format("%.2f", duration * 1.0 / (threadCount * writesPerThread)) + " ns");
        
        executor.shutdown();
        fileChannel.close();
        raf.close();
    }
    
    /**
     * 大文件分块映射演示
     */
    private static void largeFileMappingDemo(String fileName, long largeFileSize) throws IOException {
        System.out.println("大文件分块映射演示 - 文件大小: " + largeFileSize + " bytes");
        
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        FileChannel fileChannel = raf.getChannel();
        raf.setLength(largeFileSize);
        
        // 分块映射大文件
        int chunkSize = 64 * 1024 * 1024; // 64MB每块
        int chunkCount = (int) Math.ceil((double) largeFileSize / chunkSize);
        
        System.out.println("将文件分为 " + chunkCount + " 个块进行映射");
        
        for (int i = 0; i < chunkCount; i++) {
            long chunkStart = (long) i * chunkSize;
            long chunkLength = Math.min(chunkSize, largeFileSize - chunkStart);
            
            System.out.println("映射第 " + (i + 1) + " 块: 偏移量=" + chunkStart + ", 长度=" + chunkLength);
            
            MappedByteBuffer chunkBuffer = fileChannel.map(
                FileChannel.MapMode.READ_WRITE, chunkStart, chunkLength);
            
            // 在每个块的开头写入块标识
            String chunkMarker = "Chunk-" + (i + 1);
            byte[] markerBytes = chunkMarker.getBytes(StandardCharsets.UTF_8);
            chunkBuffer.put(markerBytes);
            
            chunkBuffer.force();
        }
        
        // 验证分块读取
        System.out.println("\n验证分块读取:");
        for (int i = 0; i < chunkCount; i++) {
            long chunkStart = (long) i * chunkSize;
            long chunkLength = Math.min(chunkSize, largeFileSize - chunkStart);
            
            MappedByteBuffer chunkBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, chunkStart, chunkLength);
            
            byte[] markerBytes = new byte[7]; // "Chunk-X"长度
            chunkBuffer.get(markerBytes);
            String marker = new String(markerBytes, StandardCharsets.UTF_8);
            System.out.println("第 " + (i + 1) + " 块标识: " + marker);
        }
        
        fileChannel.close();
        raf.close();
        System.out.println("大文件分块映射演示完成");
    }
    
    /**
     * 生成测试数据
     */
    private static byte[] generateTestData(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) (i % 256);
        }
        return data;
    }
    
    /**
     * 直接处理内存映射数据（模拟零拷贝处理）
     */
    private static int processDataDirectly(MappedByteBuffer buffer) {
        int processed = 0;
        int position = 0;
        
        // 模拟直接处理：计算校验和、统计信息等
        while (position < buffer.capacity() - 4) {
            int length = buffer.getInt(position);
            if (length <= 0 || position + 4 + length > buffer.capacity()) {
                break;
            }
            
            // 直接处理数据而不拷贝
            int checksum = 0;
            for (int i = 0; i < length; i++) {
                checksum += buffer.get(position + 4 + i);
            }
            
            processed += length;
            position += 4 + length;
        }
        
        return processed;
    }
}