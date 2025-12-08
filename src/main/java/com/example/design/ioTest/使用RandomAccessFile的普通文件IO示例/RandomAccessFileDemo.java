package com.example.design.ioTest.使用RandomAccessFile的普通文件IO示例;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile文件IO读写示例
 * 使用传统的文件IO操作进行读写
 */
public class RandomAccessFileDemo {
    
    public static void main(String[] args) {
        String fileName = "test_raf.dat";
        
        try {
            // 写入数据
            writeData(fileName);
            
            // 读取数据
            readData(fileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用RandomAccessFile写入数据
     */
    private static void writeData(String fileName) throws IOException {
        System.out.println("=== 使用RandomAccessFile写入数据 ===");
        
        // 创建RandomAccessFile，rw模式表示可读可写
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            
            // 写入字符串数据
            String message = "Hello, RandomAccessFile!";
            byte[] data = message.getBytes("UTF-8");
            
            // 写入数据长度（4字节）
            raf.writeInt(data.length);
            // 写入实际数据
            raf.write(data, 0, data.length);
            
            // 在文件末尾追加更多数据
            String additionalMessage = " - Additional data";
            byte[] additionalData = additionalMessage.getBytes("UTF-8");
            raf.writeInt(additionalData.length);
            raf.write(additionalData, 0, additionalData.length);
            
            System.out.println("数据写入完成，文件大小: " + raf.length() + " bytes");
        }
    }
    
    /**
     * 使用RandomAccessFile读取数据
     */
    private static void readData(String fileName) throws IOException {
        System.out.println("=== 使用RandomAccessFile读取数据 ===");
        
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
            
            // 读取第一个消息
            int dataLength = raf.readInt();
            byte[] buffer = new byte[dataLength];
            raf.readFully(buffer);
            String message1 = new String(buffer, "UTF-8");
            System.out.println("第一个消息: " + message1);
            
            // 读取第二个消息
            dataLength = raf.readInt();
            buffer = new byte[dataLength];
            raf.readFully(buffer);
            String message2 = new String(buffer, "UTF-8");
            System.out.println("第二个消息: " + message2);
            
            // 演示随机访问：跳转到文件开头重新读取
            raf.seek(0);
            dataLength = raf.readInt();
            buffer = new byte[dataLength];
            raf.readFully(buffer);
            String firstMessageAgain = new String(buffer, "UTF-8");
            System.out.println("重新读取的第一个消息: " + firstMessageAgain);
            
            System.out.println("文件总大小: " + raf.length() + " bytes");
        }
    }
}