package com.example.design.ioTest.三种零拷贝方式.直接堆外内存;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 直接缓冲区(Direct Buffer)传输Demo
 * 演示使用ByteBuffer.allocateDirect()创建直接缓冲区进行数据传输
 */
public class DirectBufferDemo {
    
    private static final int BUFFER_SIZE = 1024; // 1KB缓冲区大小
    
    public static void main(String[] args) {
        try {
            // 1. 基础直接缓冲区操作
            System.out.println("=== 1. 基础直接缓冲区操作 ===");
            basicDirectBufferDemo();
            
            // 2. 性能对比测试
            System.out.println("\n=== 2. 性能对比测试 ===");
            performanceComparisonDemo();
            
            // 3. 数据传输演示
            System.out.println("\n=== 3. 数据传输演示 ===");
            dataTransferDemo();
            
            // 4. 批量操作演示
            System.out.println("\n=== 4. 批量操作演示 ===");
            bulkOperationsDemo();
            
            // 5. 内存管理演示
            System.out.println("\n=== 5. 内存管理演示 ===");
            memoryManagementDemo();
            
            System.out.println("\n=== 所有演示完成 ===");
            
        } catch (Exception e) {
            System.err.println("演示失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 基础直接缓冲区操作
     */
    private static void basicDirectBufferDemo() {
        System.out.println("创建直接缓冲区...");
        
        // 创建直接缓冲区
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        System.out.println("直接缓冲区创建成功:");
        System.out.println("容量: " + directBuffer.capacity() + " bytes");
        System.out.println("位置: " + directBuffer.position());
        System.out.println("限制: " + directBuffer.limit());
        System.out.println("是否直接缓冲区: " + directBuffer.isDirect());
        
        // 写入数据到直接缓冲区
        System.out.println("\n--- 写入数据到直接缓冲区 ---");
        String message = "Hello, Direct Buffer! 这是直接缓冲区测试数据。";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        
        directBuffer.put(messageBytes);
        System.out.println("写入数据后位置: " + directBuffer.position());
        System.out.println("写入数据长度: " + messageBytes.length);
        
        // 切换到读取模式
        directBuffer.flip();
        System.out.println("\n切换到读取模式后:");
        System.out.println("位置: " + directBuffer.position());
        System.out.println("限制: " + directBuffer.limit());
        
        // 从直接缓冲区读取数据
        System.out.println("\n--- 从直接缓冲区读取数据 ---");
        byte[] readBytes = new byte[directBuffer.remaining()];
        directBuffer.get(readBytes);
        String readMessage = new String(readBytes, StandardCharsets.UTF_8);
        System.out.println("读取的数据: " + readMessage);
        
        // 清空缓冲区准备重新使用
        directBuffer.clear();
        System.out.println("\n清空缓冲区后位置: " + directBuffer.position());
    }
    
    /**
     * 性能对比测试：直接缓冲区 vs 堆缓冲区
     */
    private static void performanceComparisonDemo() {
        int testSize = 100000; // 10万次操作
        int dataSize = 1024;  // 每次操作1KB数据
        
        System.out.println("性能对比测试 - 操作次数: " + testSize + ", 数据大小: " + dataSize + " bytes");
        
        // 测试直接缓冲区性能
        long directStartTime = System.nanoTime();
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(dataSize);
        
        for (int i = 0; i < testSize; i++) {
            // 模拟写入操作
            byte[] testData = generateTestData(dataSize);
            directBuffer.put(testData);
            directBuffer.flip();
            
            // 模拟读取操作
            byte[] readData = new byte[dataSize];
            directBuffer.get(readData);
            
            directBuffer.clear();
        }
        
        long directEndTime = System.nanoTime();
        long directDuration = directEndTime - directStartTime;
        
        // 测试堆缓冲区性能
        long heapStartTime = System.nanoTime();
        ByteBuffer heapBuffer = ByteBuffer.allocate(dataSize);
        
        for (int i = 0; i < testSize; i++) {
            // 模拟写入操作
            byte[] testData = generateTestData(dataSize);
            heapBuffer.put(testData);
            heapBuffer.flip();
            
            // 模拟读取操作
            byte[] readData = new byte[dataSize];
            heapBuffer.get(readData);
            
            heapBuffer.clear();
        }
        
        long heapEndTime = System.nanoTime();
        long heapDuration = heapEndTime - heapStartTime;
        
        // 输出性能对比结果
        System.out.println("直接缓冲区耗时: " + TimeUnit.toMillis(directDuration) + " ms");
        System.out.println("堆缓冲区耗时: " + TimeUnit.toMillis(heapDuration) + " ms");
        System.out.println("性能提升: " + 
            String.format("%.2f", (heapDuration - directDuration) * 100.0 / heapDuration) + "%");
    }
    
    /**
     * 数据传输演示
     */
    private static void dataTransferDemo() {
        System.out.println("数据传输演示 - 模拟网络传输场景");
        
        // 创建发送端缓冲区
        ByteBuffer sendBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        
        // 准备要发送的数据
        String[] messages = {
            "第一条消息",
            "第二条较长的测试消息",
            "第三条非常重要的数据传输"
        };
        
        // 发送端：将数据写入缓冲区
        System.out.println("\n--- 发送端数据准备 ---");
        for (String message : messages) {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            
            // 写入消息长度和内容
            sendBuffer.putInt(messageBytes.length);
            sendBuffer.put(messageBytes);
            
            System.out.println("发送消息: " + message + " (长度: " + messageBytes.length + " bytes)");
        }
        
        // 切换到读取模式
        sendBuffer.flip();
        
        // 接收端：从缓冲区读取数据
        System.out.println("\n--- 接收端数据处理 ---");
        ByteBuffer receiveBuffer = sendBuffer; // 模拟数据传输
        
        while (receiveBuffer.remaining() >= 4) { // 至少要有长度信息
            // 读取消息长度
            int messageLength = receiveBuffer.getInt();
            
            if (receiveBuffer.remaining() >= messageLength) {
                // 读取消息内容
                byte[] messageBytes = new byte[messageLength];
                receiveBuffer.get(messageBytes);
                
                String receivedMessage = new String(messageBytes, StandardCharsets.UTF_8);
                System.out.println("接收消息: " + receivedMessage + " (长度: " + messageLength + " bytes)");
            } else {
                System.out.println("缓冲区数据不完整，等待更多数据...");
                break;
            }
        }
        
        System.out.println("数据传输完成，缓冲区剩余字节: " + receiveBuffer.remaining());
    }
    
    /**
     * 批量操作演示
     */
    private static void bulkOperationsDemo() {
        System.out.println("批量操作演示");
        
        // 创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        
        // 批量写入数据
        System.out.println("\n--- 批量写入数据 ---");
        byte[] largeData = generateTestData(512);
        buffer.put(largeData);
        System.out.println("批量写入 " + largeData.length + " 字节数据");
        
        // 批量读取数据
        System.out.println("\n--- 批量读取数据 ---");
        buffer.flip();
        
        byte[] readData = new byte[buffer.remaining()];
        buffer.get(readData);
        System.out.println("批量读取 " + readData.length + " 字节数据");
        
        // 验证数据完整性
        boolean dataValid = Arrays.equals(largeData, readData);
        System.out.println("数据完整性验证: " + (dataValid ? "通过" : "失败"));
        
        // 演示视图操作
        System.out.println("\n--- 缓冲区视图操作 ---");
        buffer.clear();
        
        // 写入不同类型的数据
        buffer.putInt(12345);
        buffer.putDouble(3.14159);
        buffer.putChar('A');
        
        // 创建只读视图
        ByteBuffer readOnlyView = buffer.asReadOnlyBuffer();
        buffer.flip();
        readOnlyView.flip();
        
        System.out.println("原始缓冲区可写: " + !buffer.isReadOnly());
        System.out.println("只读视图可写: " + !readOnlyView.isReadOnly());
        
        // 从只读视图读取数据
        int intValue = readOnlyView.getInt();
        double doubleValue = readOnlyView.getDouble();
        char charValue = readOnlyView.getChar();
        
        System.out.println("从只读视图读取: int=" + intValue + ", double=" + doubleValue + ", char=" + charValue);
    }
    
    /**
     * 内存管理演示
     */
    private static void memoryManagementDemo() {
        System.out.println("内存管理演示");
        
        // 演示直接缓冲区的内存分配和释放
        System.out.println("\n--- 直接缓冲区内存分配 ---");
        
        // 分配多个直接缓冲区
        int bufferCount = 5;
        ByteBuffer[] buffers = new ByteBuffer[bufferCount];
        
        for (int i = 0; i < bufferCount; i++) {
            buffers[i] = ByteBuffer.allocateDirect(64 * 1024); // 64KB每个
            System.out.println("分配直接缓冲区 " + (i + 1) + ": 64KB");
        }
        
        // 演示缓冲区复用
        System.out.println("\n--- 缓冲区复用演示 ---");
        ByteBuffer reusableBuffer = ByteBuffer.allocateDirect(128);
        
        // 第一次使用
        reusableBuffer.put("第一次使用".getBytes(StandardCharsets.UTF_8));
        reusableBuffer.flip();
        byte[] firstUse = new byte[reusableBuffer.remaining()];
        reusableBuffer.get(firstUse);
        System.out.println("第一次使用数据: " + new String(firstUse, StandardCharsets.UTF_8));
        
        // 清空后第二次使用
        reusableBuffer.clear();
        reusableBuffer.put("第二次复用".getBytes(StandardCharsets.UTF_8));
        reusableBuffer.flip();
        byte[] secondUse = new byte[reusableBuffer.remaining()];
        reusableBuffer.get(secondUse);
        System.out.println("第二次复用数据: " + new String(secondUse, StandardCharsets.UTF_8));
        
        // 演示内存监控（简化版）
        System.out.println("\n--- 内存状态监控 ---");
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("JVM已使用内存: " + (usedMemory / 1024 / 1024) + " MB");
        System.out.println("最大可用内存: " + (runtime.maxMemory() / 1024 / 1024) + " MB");
        
        // 清理缓冲区（实际中由GC管理）
        System.out.println("\n缓冲区使用完成，等待GC清理直接内存...");
        for (int i = 0; i < bufferCount; i++) {
            buffers[i] = null; // 帮助GC
        }
        
        System.gc(); // 建议GC，但不保证立即执行
        
        try {
            Thread.sleep(1000); // 给GC一点时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
    
    // 添加TimeUnit导入
    private static class TimeUnit {
        public static long toMillis(long nanos) {
            return nanos / 1_000_000;
        }
        
        public static long toMicros(long nanos) {
            return nanos / 1_000;
        }
    }
}