package com.example.design.ioTest.RandomFileDemo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile使用示例
 * 演示RandomAccessFile的基本操作：创建文件、写入数据、读取数据、随机访问等
 */
public class RandomAccessFileDemo {
    
    public static void main(String[] args) {
        String fileName = "demo_file.dat";
        
        try {
            // 1. 创建RandomAccessFile实例
            System.out.println("=== 1. 创建RandomAccessFile实例 ===");
            RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            System.out.println("文件创建成功: " + fileName);
            
            // 2. 写入基本数据类型
            System.out.println("\n=== 2. 写入基本数据类型 ===");
            writeBasicDataTypes(raf);
            
            // 3. 读取基本数据类型
            System.out.println("\n=== 3. 读取基本数据类型 ===");
            readBasicDataTypes(raf);

            // 4. 随机访问示例
            System.out.println("\n=== 4. 随机访问示例 ===");
            randomAccessExample(raf);

            // 5. 写入和读取字节数组
            System.out.println("\n=== 5. 写入和读取字节数组 ===");
            writeAndReadBytes(raf);

            // 6. 文件指针操作
            System.out.println("\n=== 6. 文件指针操作 ===");
            filePointerOperations(raf);

            // 7. 清理资源
            raf.close();
            System.out.println("\n=== 文件操作完成 ===");
            
            // 删除演示文件
            new File(fileName).delete();
            
        } catch (IOException e) {
            System.err.println("文件操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 写入基本数据类型
     */
    private static void writeBasicDataTypes(RandomAccessFile raf) throws IOException {
        // 获取当前文件指针位置
        long startPos = raf.getFilePointer();
        System.out.println("开始写入位置: " + startPos);
        
        // 写入不同类型的数据
        raf.writeBoolean(true);           // 写入布尔值
        raf.writeByte(65);                // 写入字节 (ASCII 'A')
        raf.writeChar('中');              // 写入字符
        raf.writeShort(1000);             // 写入短整型
        raf.writeInt(123456789);          // 写入整型
        raf.writeLong(9876543210L);       // 写入长整型
        raf.writeFloat(3.14f);            // 写入单精度浮点数
        raf.writeDouble(2.71828);          // 写入双精度浮点数
        raf.writeUTF("Hello, RandomAccessFile!"); // 写入UTF-8字符串
        
        long endPos = raf.getFilePointer();
        System.out.println("写入完成位置: " + endPos);
        System.out.println("总共写入字节数: " + (endPos - startPos));
    }
    
    /**
     * 读取基本数据类型
     */
    private static void readBasicDataTypes(RandomAccessFile raf) throws IOException {
        // 重置文件指针到开始位置
        raf.seek(0);
        System.out.println("重置文件指针到: " + raf.getFilePointer());
        
        // 按写入顺序读取数据
        boolean boolValue = raf.readBoolean();
        byte byteValue = raf.readByte();
        char charValue = raf.readChar();
        short shortValue = raf.readShort();
        int intValue = raf.readInt();
        long longValue = raf.readLong();
        float floatValue = raf.readFloat();
        double doubleValue = raf.readDouble();
        String stringValue = raf.readUTF();
        
        // 输出读取结果
        System.out.println("读取的布尔值: " + boolValue);
        System.out.println("读取的字节值: " + byteValue + " (ASCII: '" + (char)byteValue + "')");
        System.out.println("读取的字符: " + charValue);
        System.out.println("读取的短整型: " + shortValue);
        System.out.println("读取的整型: " + intValue);
        System.out.println("读取的长整型: " + longValue);
        System.out.println("读取的单精度浮点数: " + floatValue);
        System.out.println("读取的双精度浮点数: " + doubleValue);
        System.out.println("读取的字符串: " + stringValue);
    }
    
    /**
     * 随机访问示例
     */
    private static void randomAccessExample(RandomAccessFile raf) throws IOException {
        System.out.println("\n--- 随机访问整型数据 ---");
        
        // 直接跳转到整型数据的位置（跳过boolean, byte, char, short）
        // boolean(1) + byte(1) + char(2) + short(2) = 6字节
        raf.seek(6);
        System.out.println("跳转到整型数据位置: " + raf.getFilePointer());
        
        int intValue = raf.readInt();
        System.out.println("直接读取的整型值: " + intValue);
        
        // 修改整型值
        System.out.println("\n--- 修改整型数据 ---");
        raf.seek(6); // 回到整型数据位置
        raf.writeInt(999999999);
        
        // 验证修改结果
        raf.seek(6);
        int modifiedValue = raf.readInt();
        System.out.println("修改后的整型值: " + modifiedValue);
    }
    
    /**
     * 写入和读取字节数组
     */
    private static void writeAndReadBytes(RandomAccessFile raf) throws IOException {
        System.out.println("\n--- 写入字节数组 ---");
        
        // 移动到文件末尾
        raf.seek(raf.length());
        long startPos = raf.getFilePointer();
        System.out.println("开始写入字节数组位置: " + startPos);
        
        // 准备字节数组数据
        byte[] data = "这是字节数组数据".getBytes("UTF-8");
        System.out.println("字节数组长度: " + data.length);
        
        // 先写入数组长度
        raf.writeInt(data.length);
        // 再写入数组内容
        raf.write(data);
        
        System.out.println("字节数组写入完成");
        
        // 读取字节数组
        System.out.println("\n--- 读取字节数组 ---");
        raf.seek(startPos);
        
        int length = raf.readInt();
        System.out.println("读取的数组长度: " + length);
        
        byte[] readData = new byte[length];
        raf.readFully(readData); // 读取完整字节数组
        
        String result = new String(readData, "UTF-8");
        System.out.println("读取的字节数组内容: " + result);
    }
    
    /**
     * 文件指针操作示例
     */
    private static void filePointerOperations(RandomAccessFile raf) throws IOException {
        System.out.println("\n--- 文件指针操作 ---");
        
        // 获取文件长度
        long fileLength = raf.length();
        System.out.println("文件总长度: " + fileLength + " 字节");
        
        // 获取当前指针位置
        long currentPos = raf.getFilePointer();
        System.out.println("当前指针位置: " + currentPos);
        
        // 跳过一些字节
        raf.skipBytes(10);
        System.out.println("跳过10字节后位置: " + raf.getFilePointer());
        
        // 回到文件开头
        raf.seek(0);
        System.out.println("回到文件开头位置: " + raf.getFilePointer());
        
        // 移动到文件末尾
        raf.seek(fileLength);
        System.out.println("移动到文件末尾位置: " + raf.getFilePointer());
        
        // 在文件末尾追加数据
        raf.writeUTF("这是追加的数据");
        System.out.println("追加数据后文件长度: " + raf.length());
    }
}