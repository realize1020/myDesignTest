package com.example.design.ioTest.使用MappedByteBuffer的内存文件映射示例;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * MappedByteBuffer内存文件映射读写示例
 * 使用内存映射文件进行高性能读写操作
 */
public class MappedByteBufferDemo {
    
    public static void main(String[] args) {
        String fileName = "test_mmap.dat";
        
        try {
            // 写入数据
            writeDataWithMmap(fileName);
            
            // 读取数据
            readDataWithMmap(fileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用内存映射写入数据
     */
    private static void writeDataWithMmap(String fileName) throws IOException {
        System.out.println("=== 使用MappedByteBuffer写入数据 ===");
        
        // 文件大小：1MB
        int fileSize = 1024 * 1024;
        
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
             FileChannel channel = raf.getChannel()) {
            
            // 创建内存映射，READ_WRITE模式
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            
            // 写入数据到内存映射缓冲区
            String message1 = "Hello, Memory Mapped File!";
            byte[] data1 = message1.getBytes(StandardCharsets.UTF_8);
            
            // 写入第一个消息的长度和数据
            buffer.putInt(data1.length);
            buffer.put(data1);
            
            // 写入第二个消息
            String message2 = "This is high-performance IO!";
            byte[] data2 = message2.getBytes(StandardCharsets.UTF_8);
            
            buffer.putInt(data2.length);
            buffer.put(data2);
            
            // 强制将数据刷写到磁盘
            buffer.force();
            
            System.out.println("数据写入完成，实际写入位置: " + buffer.position());
        }
    }
    
    /**
     * 使用内存映射读取数据
     */
    private static void readDataWithMmap(String fileName) throws IOException {
        System.out.println("=== 使用MappedByteBuffer读取数据 ===");
        
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r");
             FileChannel channel = raf.getChannel()) {
            
            // 创建只读的内存映射
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            // 读取第一个消息
            int dataLength = buffer.getInt();
            byte[] data1 = new byte[dataLength];
            buffer.get(data1);
            String message1 = new String(data1, StandardCharsets.UTF_8);
            System.out.println("第一个消息: " + message1);
            
            // 读取第二个消息
            dataLength = buffer.getInt();
            byte[] data2 = new byte[dataLength];
            buffer.get(data2);
            String message2 = new String(data2, StandardCharsets.UTF_8);
            System.out.println("第二个消息: " + message2);
            
            // 演示内存映射的随机访问特性
            // 重新定位到开头读取第一个消息
            buffer.position(0);
            dataLength = buffer.getInt();
            byte[] data1Again = new byte[dataLength];
            buffer.get(data1Again);
            String firstMessageAgain = new String(data1Again, StandardCharsets.UTF_8);
            System.out.println("重新读取的第一个消息: " + firstMessageAgain);
            
            System.out.println("文件总大小: " + channel.size() + " bytes");
        }
    }
    
    /**
     * 高级示例：演示内存映射的性能优势（大文件处理）
     */
    private static void advancedMmapDemo(String fileName) throws IOException {
        System.out.println("=== 高级内存映射示例：大文件处理 ===");
        
        int fileSize = 10 * 1024 * 1024; // 10MB
        
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
             FileChannel channel = raf.getChannel()) {
            
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            
            // 批量写入数据
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < 1000; i++) {
                String data = "Message " + i + ": This is a test message for performance comparison.";
                byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                
                // 写入长度和数据
                buffer.putInt(bytes.length);
                buffer.put(bytes);
            }
            
            buffer.force(); // 确保数据刷写到磁盘
            
            long endTime = System.currentTimeMillis();
            System.out.println("内存映射写入1000条消息耗时: " + (endTime - startTime) + "ms");
            
            // 批量读取数据
            startTime = System.currentTimeMillis();
            buffer.position(0);
            
            for (int i = 0; i < 1000; i++) {
                int length = buffer.getInt();
                byte[] bytes = new byte[length];
                buffer.get(bytes);
                // 这里可以处理读取到的数据
            }
            
            endTime = System.currentTimeMillis();
            System.out.println("内存映射读取1000条消息耗时: " + (endTime - startTime) + "ms");
        }
    }
}