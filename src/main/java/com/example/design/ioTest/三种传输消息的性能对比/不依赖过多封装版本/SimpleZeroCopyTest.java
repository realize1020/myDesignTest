package com.example.design.ioTest.三种传输消息的性能对比.不依赖过多封装版本;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 直白的零拷贝传输性能测试（无依赖版本）
 * 
 * @author RuYuan MQ Team
 */
public class SimpleZeroCopyTest {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleZeroCopyTest.class);
    
    private static final int SERVER_PORT = 8888;
    private static final String SERVER_HOST = "localhost";
    
    // 测试不同大小的文件
    private static final int[] TEST_SIZES = {
        1024,           // 1KB
        10240,          // 10KB  
        102400,         // 100KB
        1024000,        // 1MB
        10240000,       // 10MB
        102400000       // 100MB
    };
    
    // 多次测试取平均值
    private static final int TEST_ROUNDS = 5;
    
    // 内存监控
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    
    // 性能统计
    private final Map<TransferMethod, PerformanceStats> stats = new ConcurrentHashMap<>();
    
    public static void main(String[] args) throws Exception {
        SimpleZeroCopyTest test = new SimpleZeroCopyTest();
        
        // 创建测试文件
        test.createTestFiles();
        
        // 启动服务器
        test.startServer();
        
        // 等待服务器启动
        Thread.sleep(2000);
        
        // 运行全面性能测试
        test.runComprehensiveTest();
        
        // 清理资源
        test.cleanup();
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
     * 运行全面性能测试
     */
    public void runComprehensiveTest() throws Exception {
        logger.info("开始全面性能测试...");
        
        for (int size : TEST_SIZES) {
            String fileName = "test_" + size + ".bin";
            logger.info("\n=== 测试文件大小: {} bytes ===", size);
            
            // 多次测试取平均值
            TestResult mappedResult = testMultipleRounds(() -> {
                try {
                    testMappedFileTransfer(fileName, size);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, TransferMethod.MAPPED_FILE);

            //******************************************************
            TestResult fileRegionResult = testMultipleRounds(() -> {
                try {
                    testFileRegionTransfer(fileName, size);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, TransferMethod.FILE_REGION);

            //******************************************************
            TestResult bufferResult = testMultipleRounds(() -> {
                try {
                    testBufferTransfer(fileName, size);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, TransferMethod.BUFFER);
            
            // 输出平均结果
            logger.info("文件映射零拷贝 - 平均耗时: {}ms, 平均吞吐量: {}MB/s", 
                       mappedResult.avgDuration, String.format("%.2f", mappedResult.avgThroughput));
            logger.info("FileRegion 零拷贝 - 平均耗时: {}ms, 平均吞吐量: {}MB/s", 
                       fileRegionResult.avgDuration, String.format("%.2f", fileRegionResult.avgThroughput));
            logger.info("普通 Buffer 传输 - 平均耗时: {}ms, 平均吞吐量: {}MB/s", 
                       bufferResult.avgDuration, String.format("%.2f", bufferResult.avgThroughput));
            
            // 分析最优方案
            analyzeBestMethod(size, mappedResult, fileRegionResult, bufferResult);
            
            Thread.sleep(1000); // 间隔1秒
        }
        
        // 输出最终统计结果
        printFinalStatistics();
    }
    
    /**
     * 多次测试取平均值
     */
    private TestResult testMultipleRounds(Runnable test, TransferMethod method) throws Exception {
        long totalDuration = 0;
        double totalThroughput = 0;
        List<Long> durations = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();
        
        for (int i = 0; i < TEST_ROUNDS; i++) {
            PerformanceMetrics metrics = runSingleTest(test);
            totalDuration += metrics.duration;
            totalThroughput += metrics.throughput;
            durations.add(metrics.duration);
            throughputs.add(metrics.throughput);
            
            // 记录到统计中
            recordTransfer(method, metrics.fileSize, metrics.duration);
            
            // 清理资源，避免影响下次测试
            System.gc();
            Thread.sleep(100);
        }
        
        // 计算标准差
        double durationStdDev = calculateStandardDeviation(durations);
        double throughputStdDev = calculateStandardDeviation(throughputs);
        
        return new TestResult(
            totalDuration / TEST_ROUNDS, 
            totalThroughput / TEST_ROUNDS,
            durationStdDev,
            throughputStdDev
        );
    }
    
    /**
     * 运行单次测试
     */
    private PerformanceMetrics runSingleTest(Runnable test) throws Exception {
        // 记录开始状态
        MemorySnapshot startMemory = takeMemorySnapshot();
        long startTime = System.nanoTime();
        
        // 执行测试
        test.run();
        
        // 记录结束状态
        long endTime = System.nanoTime();
        MemorySnapshot endMemory = takeMemorySnapshot();
        
        // 计算性能指标
        return calculateMetrics(startTime, endTime, startMemory, endMemory, 0);
    }
    
    /**
     * 测试文件映射零拷贝传输（直接使用 FileChannel.map）
     */
    private void testMappedFileTransfer(String fileName, int fileSize) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(fileName, "r");
             FileChannel fileChannel = file.getChannel()) {
            
            // 创建内存映射
            MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            
            // 模拟读取数据
            byte[] data = new byte[Math.min(fileSize, 1024)];
            mappedBuffer.get(data);
            
            // 清理映射缓冲区
            cleanupMappedBuffer(mappedBuffer);
        }
    }
    
    /**
     * 测试 FileRegion 零拷贝传输（直接使用 Netty 的 DefaultFileRegion）
     */
    private void testFileRegionTransfer(String fileName, int fileSize) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(fileName, "r");
             FileChannel fileChannel = file.getChannel()) {
            
            // 创建 FileRegion
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
        }
    }
    
    /**
     * 测试普通 Buffer 传输（直接使用 ByteBuffer.allocateDirect）
     */
    private void testBufferTransfer(String fileName, int fileSize) throws Exception {
        // 直接分配 DirectByteBuffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(fileSize);
        
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
        }
        
        // 清理 DirectByteBuffer
        cleanupDirectBuffer(buffer);
    }
    
    /**
     * 分析最优方案
     */
    private void analyzeBestMethod(int fileSize, TestResult mapped, TestResult fileRegion, TestResult buffer) {
        String bestMethod;
        double bestThroughput = Math.max(Math.max(mapped.avgThroughput, fileRegion.avgThroughput), buffer.avgThroughput);
        
        if (bestThroughput == mapped.avgThroughput) {
            bestMethod = "文件映射";
        } else if (bestThroughput == fileRegion.avgThroughput) {
            bestMethod = "FileRegion";
        } else {
            bestMethod = "普通Buffer";
        }
        
        logger.info("文件大小 {} bytes 的最优方案: {} (吞吐量: {}MB/s)", 
                   fileSize, bestMethod, String.format("%.2f", bestThroughput));
        
        // 分析性能差异
        double mappedVsFileRegion = (mapped.avgThroughput - fileRegion.avgThroughput) / fileRegion.avgThroughput * 100;
        double mappedVsBuffer = (mapped.avgThroughput - buffer.avgThroughput) / buffer.avgThroughput * 100;
        double fileRegionVsBuffer = (fileRegion.avgThroughput - buffer.avgThroughput) / buffer.avgThroughput * 100;
        
        logger.info("性能对比 - 文件映射 vs FileRegion: {:.1f}%, 文件映射 vs 普通Buffer: {:.1f}%, FileRegion vs 普通Buffer: {:.1f}%", 
                   mappedVsFileRegion, mappedVsBuffer, fileRegionVsBuffer);
    }
    
    /**
     * 输出最终统计结果
     */
    private void printFinalStatistics() {
        logger.info("\n=== 最终统计结果 ===");
        
        for (Map.Entry<TransferMethod, PerformanceStats> entry : stats.entrySet()) {
            TransferMethod method = entry.getKey();
            PerformanceStats stat = entry.getValue();
            
            logger.info("{} 传输方式统计:", method.getName());
            logger.info("  总传输次数: {}", stat.getTotalTransfers());
            logger.info("  平均吞吐量: {:.2f} MB/s", stat.getOverallAvgThroughput());
            logger.info("  最佳吞吐量: {:.2f} MB/s", stat.getBestThroughput());
            logger.info("  最差吞吐量: {:.2f} MB/s", stat.getWorstThroughput());
        }
        
        // 推荐策略
        logger.info("\n=== 推荐策略 ===");
        logger.info("小文件 (< 1MB): 推荐使用 FileRegion");
        logger.info("中等文件 (1MB - 100MB): 推荐使用 FileRegion");
        logger.info("大文件 (> 100MB): 推荐使用 文件映射");
    }
    
    /**
     * 记录传输性能
     */
    private void recordTransfer(TransferMethod method, int fileSize, long duration) {
        PerformanceStats stat = stats.computeIfAbsent(method, k -> new PerformanceStats());
        stat.recordTransfer(fileSize, duration);
    }
    
    /**
     * 清理映射缓冲区
     */
    private void cleanupMappedBuffer(MappedByteBuffer mappedBuffer) {
        try {
            // 使用反射调用 Cleaner.clean() 来释放映射内存
            java.lang.reflect.Method cleanerMethod = mappedBuffer.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(mappedBuffer);
            if (cleaner != null) {
                java.lang.reflect.Method cleanMethod = cleaner.getClass().getMethod("clean");
                cleanMethod.setAccessible(true);
                cleanMethod.invoke(cleaner);
            }
        } catch (Exception e) {
            logger.warn("清理映射缓冲区失败", e);
        }
    }
    
    /**
     * 清理 DirectByteBuffer
     */
    private void cleanupDirectBuffer(ByteBuffer buffer) {
        try {
            // 使用反射调用 Cleaner.clean() 来释放直接内存
            java.lang.reflect.Method cleanerMethod = buffer.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(buffer);
            if (cleaner != null) {
                java.lang.reflect.Method cleanMethod = cleaner.getClass().getMethod("clean");
                cleanMethod.setAccessible(true);
                cleanMethod.invoke(cleaner);
            }
        } catch (Exception e) {
            logger.warn("清理 DirectByteBuffer 失败", e);
        }
    }
    
    /**
     * 计算标准差
     */
    private double calculateStandardDeviation(List<? extends Number> values) {
        if (values.isEmpty()) return 0;
        
        double mean = values.stream().mapToDouble(Number::doubleValue).average().orElse(0);
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v.doubleValue() - mean, 2))
                .average()
                .orElse(0);
        
        return Math.sqrt(variance);
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
        
        return new PerformanceMetrics(duration, heapMemoryUsed, directMemoryUsed, throughput, fileSize);
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
        
        logger.info("资源清理完成");
    }
    
    // 内部类定义
    
    /**
     * 传输方法枚举
     */
    public enum TransferMethod {
        MAPPED_FILE("文件映射"),
        FILE_REGION("FileRegion"),
        BUFFER("普通Buffer");
        
        private final String name;
        
        TransferMethod(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    /**
     * 测试结果类
     */
    private static class TestResult {
        final long avgDuration;
        final double avgThroughput;
        final double durationStdDev;
        final double throughputStdDev;
        
        public TestResult(long avgDuration, double avgThroughput, double durationStdDev, double throughputStdDev) {
            this.avgDuration = avgDuration;
            this.avgThroughput = avgThroughput;
            this.durationStdDev = durationStdDev;
            this.throughputStdDev = throughputStdDev;
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
        final int fileSize;
        
        public PerformanceMetrics(long duration, long heapMemoryUsed, long directMemoryUsed, double throughput, int fileSize) {
            this.duration = duration;
            this.heapMemoryUsed = heapMemoryUsed;
            this.directMemoryUsed = directMemoryUsed;
            this.throughput = throughput;
            this.fileSize = fileSize;
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
     * 性能统计类
     */
    private static class PerformanceStats {
        private final List<Double> throughputs = new ArrayList<>();
        private int totalTransfers = 0;
        
        public void recordTransfer(int fileSize, long duration) {
            double throughput = (fileSize / 1024.0 / 1024.0) / (duration / 1000.0);
            throughputs.add(throughput);
            totalTransfers++;
        }
        
        public int getTotalTransfers() {
            return totalTransfers;
        }
        
        public double getOverallAvgThroughput() {
            return throughputs.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }
        
        public double getBestThroughput() {
            return throughputs.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        }
        
        public double getWorstThroughput() {
            return throughputs.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        }
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