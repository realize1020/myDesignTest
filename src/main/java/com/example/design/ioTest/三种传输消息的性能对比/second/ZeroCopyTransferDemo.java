package com.example.design.ioTest.三种传输消息的性能对比.second;

import com.example.design.ioTest.三种传输消息的性能对比.first.DirectBufferPool;
import com.example.design.ioTest.三种传输消息的性能对比.first.MappedFileManager;
import com.example.design.ioTest.三种传输消息的性能对比.first.MessageLocation;
import com.example.design.ioTest.三种传输消息的性能对比.first.ZeroCopyMessageTransfer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 零拷贝传输性能测试 Demo（内存统计修复版）
 * 
 * @author RuYuan MQ Team
 */
public class ZeroCopyTransferDemo {
    
    private static final Logger logger = LoggerFactory.getLogger(ZeroCopyTransferDemo.class);
    
    private static final int SERVER_PORT = 8888;
    private static final String SERVER_HOST = "localhost";
    
    // 测试文件大小配置
    private static final int[] TEST_SIZES = {1024, 10240, 102400, 1024000}; // 1KB, 10KB, 100KB, 1MB
    
    // 内存监控
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    
    public static void main(String[] args) throws Exception {
        ZeroCopyTransferDemo demo = new ZeroCopyTransferDemo();
        
        // 创建测试文件
        demo.createTestFiles();
        
        // 启动服务器
        demo.startServer();
        
        // 等待服务器启动
        Thread.sleep(2000);
        
        // 运行性能测试
        demo.runPerformanceTest();
        
        // 清理资源
        demo.cleanup();
    }
    
    /**
     * 创建不同大小的测试文件
     */
    private void createTestFiles() throws IOException {
        logger.info("创建测试文件...");
        
        for (int size : TEST_SIZES) {
            String fileName = "test_" + size + ".bin";
            createTestFile(fileName, size);
        }
        
        logger.info("测试文件创建完成");
    }
    
    /**
     * 创建指定大小的测试文件
     */
    private void createTestFile(String fileName, int size) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            
            byte[] data = new byte[1024];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (i % 256);
            }
            
            int remaining = size;
            while (remaining > 0) {
                int writeSize = Math.min(remaining, data.length);
                bos.write(data, 0, writeSize);
                remaining -= writeSize;
            }
            
            logger.debug("创建测试文件: {} ({} bytes)", fileName, size);
        }
    }
    
    /**
     * 启动测试服务器
     */
    private void startServer() {
        new Thread(() -> {
            try {
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(new TestServerHandler());
                            }
                        });
                
                ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
                logger.info("测试服务器启动成功，端口: {}", SERVER_PORT);
                
                future.channel().closeFuture().sync();
                
            } catch (Exception e) {
                logger.error("服务器启动失败", e);
            }
        }).start();
    }
    
    /**
     * 运行性能测试
     */
    private void runPerformanceTest() throws Exception {
        logger.info("开始性能测试...");
        
        for (int size : TEST_SIZES) {
            String fileName = "test_" + size + ".bin";
            logger.info("\n=== 测试文件大小: {} bytes ===", size);
            
            // 测试文件映射零拷贝
            testMappedFileTransfer(fileName, size);
            
            // 测试 FileRegion 零拷贝
            testFileRegionTransfer(fileName, size);
            
            // 测试普通 Buffer 传输
            testBufferTransfer(fileName, size);
            
            Thread.sleep(1000); // 间隔1秒
        }
        
        logger.info("\n性能测试完成！");
    }
    
    /**
     * 测试文件映射零拷贝传输
     */
    private void testMappedFileTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试文件映射零拷贝传输...");
        
        // 创建文件映射
        MappedFileManager fileManager = MappedFileManager.getInstance();
        MessageLocation location = new MessageLocation(fileName, 0, fileSize);
        
        // 记录开始状态
        MemorySnapshot startMemory = takeMemorySnapshot();
        long startTime = System.nanoTime();
        
        // 模拟传输
        MappedByteBuffer mappedBuffer = fileManager.map(location);
        if (mappedBuffer != null) {
            // 模拟读取数据
            byte[] data = new byte[Math.min(fileSize, 1024)];
            mappedBuffer.get(data);
        }
        
        // 记录结束状态
        long endTime = System.nanoTime();
        MemorySnapshot endMemory = takeMemorySnapshot();
        
        // 计算性能指标
        PerformanceMetrics metrics = calculateMetrics(startTime, endTime, startMemory, endMemory, fileSize);
        
        logger.info("文件映射零拷贝 - 耗时: {}ms, 堆内存: {}KB, 直接内存: {}KB, 吞吐量: {}MB/s", 
                   metrics.duration, metrics.heapMemoryUsed, metrics.directMemoryUsed, metrics.throughput);
        
        // 清理映射
        fileManager.removeMapping(fileName);
    }
    
    /**
     * 测试 FileRegion 零拷贝传输
     */
    private void testFileRegionTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试 FileRegion 零拷贝传输...");
        
        // 记录开始状态
        MemorySnapshot startMemory = takeMemorySnapshot();
        long startTime = System.nanoTime();
        
        // 创建 FileRegion
        try (RandomAccessFile file = new RandomAccessFile(fileName, "r");
             FileChannel fileChannel = file.getChannel()) {
            
            FileRegion fileRegion = new DefaultFileRegion(fileChannel, 0, fileSize);
            
            // 模拟传输 - 使用 ByteBuffer 来模拟网络传输
            ByteBuffer buffer = ByteBuffer.allocateDirect(fileSize);
            long transferred = 0;
            
            // 模拟从文件读取到缓冲区
            while (transferred < fileSize) {
                int bytesRead = fileChannel.read(buffer, transferred);
                if (bytesRead == -1) break;
                transferred += bytesRead;
            }
            
            // 记录结束状态
            long endTime = System.nanoTime();
            MemorySnapshot endMemory = takeMemorySnapshot();
            
            // 计算性能指标
            PerformanceMetrics metrics = calculateMetrics(startTime, endTime, startMemory, endMemory, fileSize);
            
            logger.info("FileRegion 零拷贝 - 耗时: {}ms, 堆内存: {}KB, 直接内存: {}KB, 吞吐量: {}MB/s", 
                       metrics.duration, metrics.heapMemoryUsed, metrics.directMemoryUsed, metrics.throughput);
        }
    }
    
    /**
     * 测试普通 Buffer 传输
     */
    private void testBufferTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试普通 Buffer 传输...");
        
        DirectBufferPool bufferPool = DirectBufferPool.getInstance();
        
        // 记录开始状态
        MemorySnapshot startMemory = takeMemorySnapshot();
        long startTime = System.nanoTime();
        
        // 使用 DirectBufferPool
        ByteBuffer buffer = bufferPool.acquire(fileSize);
        try {
            try (RandomAccessFile file = new RandomAccessFile(fileName, "r");
                 FileChannel fileChannel = file.getChannel()) {
                
                buffer.clear();
                int bytesRead = fileChannel.read(buffer);
                
                if (bytesRead > 0) {
                    buffer.flip();
                    // 模拟处理数据
                    byte[] data = new byte[Math.min(bytesRead, 1024)];
                    buffer.get(data);
                }
                
                // 记录结束状态
                long endTime = System.nanoTime();
                MemorySnapshot endMemory = takeMemorySnapshot();
                
                // 计算性能指标
                PerformanceMetrics metrics = calculateMetrics(startTime, endTime, startMemory, endMemory, fileSize);
                
                logger.info("普通 Buffer 传输 - 耗时: {}ms, 堆内存: {}KB, 直接内存: {}KB, 吞吐量: {}MB/s", 
                           metrics.duration, metrics.heapMemoryUsed, metrics.directMemoryUsed, metrics.throughput);
            }
        } finally {
            bufferPool.release(buffer);
        }
    }
    
    /**
     * 内存快照类
     */
    private static class MemorySnapshot {
        final long heapUsed;
        final long heapMax;
        final long directMemoryUsed;
        final long directMemoryMax;
        
        public MemorySnapshot(long heapUsed, long heapMax, long directMemoryUsed, long directMemoryMax) {
            this.heapUsed = heapUsed;
            this.heapMax = heapMax;
            this.directMemoryUsed = directMemoryUsed;
            this.directMemoryMax = directMemoryMax;
        }
    }
    
    /**
     * 性能指标类
     */
    private static class PerformanceMetrics {
        final long duration;
        final long heapMemoryUsed;
        final long directMemoryUsed;
        final double throughput;
        
        public PerformanceMetrics(long duration, long heapMemoryUsed, long directMemoryUsed, double throughput) {
            this.duration = duration;
            this.heapMemoryUsed = heapMemoryUsed;
            this.directMemoryUsed = directMemoryUsed;
            this.throughput = throughput;
        }
    }
    
    /**
     * 获取内存快照
     */
    private MemorySnapshot takeMemorySnapshot() {
        // 强制 GC 以获得更准确的内存使用情况
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 获取堆内存使用情况
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        long heapUsed = heapUsage.getUsed();
        long heapMax = heapUsage.getMax();
        
        // 获取直接内存使用情况（通过反射）
        long directMemoryUsed = getDirectMemoryUsed();
        long directMemoryMax = getDirectMemoryMax();
        
        return new MemorySnapshot(heapUsed, heapMax, directMemoryUsed, directMemoryMax);
    }
    
    /**
     * 获取直接内存使用量
     */
    private long getDirectMemoryUsed() {
        try {
            // 使用反射获取 DirectMemory 使用量
            Class<?> clazz = Class.forName("java.nio.Bits");
            java.lang.reflect.Field field = clazz.getDeclaredField("reservedMemory");
            field.setAccessible(true);
            return (Long) field.get(null);
        } catch (Exception e) {
            // 如果无法获取，返回 0
            return 0;
        }
    }
    
    /**
     * 获取直接内存最大值
     */
    private long getDirectMemoryMax() {
        try {
            // 使用反射获取 DirectMemory 最大值
            Class<?> clazz = Class.forName("java.nio.Bits");
            java.lang.reflect.Field field = clazz.getDeclaredField("maxMemory");
            field.setAccessible(true);
            return (Long) field.get(null);
        } catch (Exception e) {
            // 如果无法获取，返回 0
            return 0;
        }
    }
    
    /**
     * 计算性能指标
     */
    private PerformanceMetrics calculateMetrics(long startTime, long endTime, 
                                               MemorySnapshot startMemory, MemorySnapshot endMemory, 
                                               int fileSize) {
        long duration = (endTime - startTime) / 1_000_000; // 转换为毫秒
        
        long heapMemoryUsed = (endMemory.heapUsed - startMemory.heapUsed) / 1024; // 转换为KB
        long directMemoryUsed = (endMemory.directMemoryUsed - startMemory.directMemoryUsed) / 1024; // 转换为KB
        
        double throughput = 0;
        if (duration > 0) {
            throughput = (fileSize / 1024.0 / 1024.0) / (duration / 1000.0); // MB/s
        }
        
        return new PerformanceMetrics(duration, heapMemoryUsed, directMemoryUsed, throughput);
    }
    
    /**
     * 清理资源
     */
    private void cleanup() {
        logger.info("清理测试资源...");
        
        // 清理测试文件
        for (int size : TEST_SIZES) {
            String fileName = "test_" + size + ".bin";
            try {
                Files.deleteIfExists(Paths.get(fileName));
            } catch (IOException e) {
                logger.warn("删除测试文件失败: {}", fileName, e);
            }
        }
        
        // 清理零拷贝组件
        ZeroCopyMessageTransfer.getInstance().cleanup();
        DirectBufferPool.getInstance().cleanup();
        MappedFileManager.getInstance().cleanup();
        
        logger.info("资源清理完成");
    }
    
    /**
     * 测试服务器处理器
     */
    private static class TestServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof FileInfo) {
                FileInfo fileInfo = (FileInfo) msg;
                logger.info("收到文件传输请求: {}", fileInfo);
            }
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("服务器处理异常", cause);
            ctx.close();
        }
    }
    
    /**
     * 测试客户端处理器
     */
    private static class TestClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("客户端连接成功");
        }
        
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            logger.info("收到服务器响应: {}", msg);
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("客户端处理异常", cause);
            ctx.close();
        }
    }
    
    /**
     * 文件信息类
     */
    private static class FileInfo {
        private final String fileName;
        private final int fileSize;
        
        public FileInfo(String fileName, int fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }
        
        public String getFileName() { return fileName; }
        public int getFileSize() { return fileSize; }
        
        @Override
        public String toString() {
            return "FileInfo{fileName='" + fileName + "', fileSize=" + fileSize + "}";
        }
    }
}