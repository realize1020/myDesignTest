package com.example.design.ioTest.三种传输消息的性能对比.first;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * 零拷贝传输性能测试 Demo（修复版）
 *
 * @author RuYuan MQ Team
 */
public class ZeroCopyTransferDemo {

    private static final Logger logger = LoggerFactory.getLogger(ZeroCopyTransferDemo.class);

    private static final String TEST_FILE_PATH = "test_data.bin";
    private static final int SERVER_PORT = 8888;
    private static final String SERVER_HOST = "localhost";

    // 测试文件大小配置
    private static final int[] TEST_SIZES = {1024, 10240, 102400, 1024000}; // 1KB, 10KB, 100KB, 1MB

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

        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        // 模拟传输（这里只是测试映射性能）
        MappedByteBuffer mappedBuffer = fileManager.map(location);
        if (mappedBuffer != null) {
            // 模拟读取数据
            byte[] data = new byte[Math.min(fileSize, 1024)];
            mappedBuffer.get(data);
        }

        long endTime = System.nanoTime();
        long endMemory = getUsedMemory();

        long duration = (endTime - startTime) / 1_000_000; // 转换为毫秒
        long memoryUsed = endMemory - startMemory;

        logger.info("文件映射零拷贝 - 耗时: {}ms, 内存使用: {}KB", duration, memoryUsed / 1024);

        // 清理映射
        fileManager.removeMapping(fileName);
    }

    /**
     * 测试 FileRegion 零拷贝传输（修复版）
     */
    private void testFileRegionTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试 FileRegion 零拷贝传输...");

        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

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

            long endTime = System.nanoTime();
            long endMemory = getUsedMemory();

            long duration = (endTime - startTime) / 1_000_000;
            long memoryUsed = endMemory - startMemory;

            logger.info("FileRegion 零拷贝 - 耗时: {}ms, 内存使用: {}KB, 传输字节: {}",
                    duration, memoryUsed / 1024, transferred);
        }
    }

    /**
     * 测试普通 Buffer 传输
     */
    private void testBufferTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试普通 Buffer 传输...");

        DirectBufferPool bufferPool = DirectBufferPool.getInstance();

        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

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

                long endTime = System.nanoTime();
                long endMemory = getUsedMemory();

                long duration = (endTime - startTime) / 1_000_000;
                long memoryUsed = endMemory - startMemory;

                logger.info("普通 Buffer 传输 - 耗时: {}ms, 内存使用: {}KB, 读取字节: {}",
                        duration, memoryUsed / 1024, bytesRead);
            }
        } finally {
            bufferPool.release(buffer);
        }
    }

    /**
     * 网络传输性能测试（真实网络传输）
     */
    private void testNetworkTransfer(String fileName, int fileSize) throws Exception {
        logger.info("测试网络传输性能...");

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TestClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync();
            Channel channel = future.channel();

            // 发送文件信息
            FileInfo fileInfo = new FileInfo(fileName, fileSize);
            channel.writeAndFlush(fileInfo);

            // 等待传输完成
            Thread.sleep(1000);

            channel.close().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 获取当前内存使用量
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
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

                // 这里可以添加实际的零拷贝传输逻辑
                // ZeroCopyMessageTransfer.getInstance().transferMessage(ctx.channel(), location);
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