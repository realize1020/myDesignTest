package com.example.design.ioTest.三种零拷贝方式.FileRegion零拷贝;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.channel.FileRegion;
import io.netty.channel.DefaultFileRegion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * FileRegion零拷贝文件传输Demo
 * 演示使用Netty的DefaultFileRegion实现高性能零拷贝文件传输
 */
public class FileRegionDemo {
    
    private static final int PORT = 8888;
    private static final String HOST = "localhost";
    private static final String TEST_FILE = "test_file.dat";
    private static final int FILE_SIZE = 10 * 1024 * 1024; // 10MB测试文件
    
    public static void main(String[] args) throws Exception {
        // 创建测试文件
        createTestFile();
        
        try {
            // 1. 基础FileRegion传输演示
            System.out.println("=== 1. 基础FileRegion传输演示 ===");
            basicFileRegionDemo();
            
            // 2. 高性能文件服务器演示
            System.out.println("\n=== 2. 高性能文件服务器演示 ===");
            fileServerDemo();
            
            // 3. 大文件分块传输演示
            System.out.println("\n=== 3. 大文件分块传输演示 ===");
            chunkedFileTransferDemo();
            
            System.out.println("\n=== 所有演示完成 ===");
            
        } finally {
            // 清理测试文件
            new File(TEST_FILE).delete();
            new File("received_file.dat").delete();
            new File("chunked_received.dat").delete();
        }
    }
    
    /**
     * 创建测试文件
     */
    private static void createTestFile() throws IOException {
        System.out.println("创建测试文件: " + TEST_FILE + " (" + FILE_SIZE + " bytes)");
        
        try (FileOutputStream fos = new FileOutputStream(TEST_FILE)) {
            byte[] data = new byte[1024];
            for (int i = 0; i < FILE_SIZE / 1024; i++) {
                // 填充测试数据
                for (int j = 0; j < 1024; j++) {
                    data[j] = (byte) ((i + j) % 256);
                }
                fos.write(data);
            }
        }
        System.out.println("测试文件创建完成");
    }
    
    /**
     * 基础FileRegion传输演示
     */
    private static void basicFileRegionDemo() throws Exception {
        System.out.println("基础FileRegion传输演示");
        
        // 创建文件通道
        RandomAccessFile file = new RandomAccessFile(TEST_FILE, "r");
        FileChannel fileChannel = file.getChannel();
        
        // 创建DefaultFileRegion
        FileRegion fileRegion = new DefaultFileRegion(
            fileChannel, 0, fileChannel.size());
        
        System.out.println("FileRegion信息:");
        System.out.println("文件大小: " + fileRegion.count() + " bytes");
        System.out.println("传输位置: " + fileRegion.position());
        
        // 演示传输统计
        System.out.println("\n模拟传输统计:");
        long transferred = fileRegion.transferred();
        System.out.println("已传输字节: " + transferred);
        
        // 清理资源
        fileChannel.close();
        file.close();
        
        System.out.println("基础演示完成");
    }
    
    /**
     * 高性能文件服务器演示
     */
    private static void fileServerDemo() throws Exception {
        System.out.println("启动文件服务器和客户端进行零拷贝传输测试...");
        
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch transferComplete = new CountDownLatch(1);
        
        // 启动文件服务器
        Thread serverThread = new Thread(() -> {
            try {
                startFileServer(serverReady, transferComplete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        
        // 等待服务器启动
        serverReady.await();
        
        // 启动文件客户端
        Thread clientThread = new Thread(() -> {
            try {
                startFileClient(transferComplete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
        
        // 等待传输完成
        transferComplete.await(30, TimeUnit.SECONDS);
        
        // 验证文件传输结果
        File receivedFile = new File("received_file.dat");
        if (receivedFile.exists()) {
            long originalSize = new File(TEST_FILE).length();
            long receivedSize = receivedFile.length();
            boolean success = originalSize == receivedSize;
            
            System.out.println("文件传输验证:");
            System.out.println("原始文件大小: " + originalSize + " bytes");
            System.out.println("接收文件大小: " + receivedSize + " bytes");
            System.out.println("传输结果: " + (success ? "成功" : "失败"));
        }
        
        serverThread.interrupt();
        clientThread.interrupt();
    }
    
    /**
     * 启动文件服务器
     */
    private static void startFileServer(CountDownLatch serverReady, CountDownLatch transferComplete) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                         new LoggingHandler(LogLevel.INFO),
                         new FileServerHandler(transferComplete)
                     );
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            ChannelFuture f = b.bind(PORT).sync();
            System.out.println("文件服务器启动在端口: " + PORT);
            serverReady.countDown();
            
            f.channel().closeFuture().sync();
            
        } catch (InterruptedException e) {
            System.out.println("服务器被中断");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    /**
     * 文件服务器处理器
     */
    private static class FileServerHandler extends ChannelInboundHandlerAdapter {
        private final CountDownLatch transferComplete;
        
        public FileServerHandler(CountDownLatch transferComplete) {
            this.transferComplete = transferComplete;
        }
        
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端连接建立，开始零拷贝文件传输...");
            
            try {
                // 创建文件通道
                RandomAccessFile file = new RandomAccessFile(TEST_FILE, "r");
                FileChannel fileChannel = file.getChannel();
                
                // 创建FileRegion进行零拷贝传输
                FileRegion fileRegion = new DefaultFileRegion(
                    fileChannel, 0, fileChannel.size());
                
                // 发送文件大小信息（可选）
                ctx.writeAndFlush(new FileInfoMessage(fileChannel.size()));
                
                // 零拷贝传输文件内容
                ChannelFuture transferFuture = ctx.writeAndFlush(fileRegion);
                
                transferFuture.addListener(future -> {
                    try {
                        if (future.isSuccess()) {
                            System.out.println("文件传输完成: " + fileChannel.size() + " bytes");
                        } else {
                            System.out.println("文件传输失败: " + future.cause().getMessage());
                        }
                        
                        fileChannel.close();
                        file.close();
                        transferComplete.countDown();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                
            } catch (IOException e) {
                System.out.println("文件传输错误: " + e.getMessage());
                transferComplete.countDown();
            }
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
            transferComplete.countDown();
        }
    }
    
    /**
     * 启动文件客户端
     */
    private static void startFileClient(CountDownLatch transferComplete) {
        EventLoopGroup group = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                         new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4),
                         new LengthFieldPrepender(4),
                         new FileClientHandler()
                     );
                 }
             });
            
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().closeFuture().sync();
            
        } catch (Exception e) {
            System.out.println("客户端错误: " + e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
    
    /**
     * 文件客户端处理器
     */
    private static class FileClientHandler extends ChannelInboundHandlerAdapter {
        private FileOutputStream fos;
        private long totalBytes;
        private long receivedBytes;
        
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("连接到文件服务器，准备接收文件...");
            fos = new FileOutputStream("received_file.dat");
        }
        
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FileInfoMessage) {
                // 接收文件信息
                FileInfoMessage fileInfo = (FileInfoMessage) msg;
                totalBytes = fileInfo.getFileSize();
                System.out.println("准备接收文件，大小: " + totalBytes + " bytes");
                
            } else if (msg instanceof io.netty.buffer.ByteBuf) {
                // 接收文件数据
                io.netty.buffer.ByteBuf buf = (io.netty.buffer.ByteBuf) msg;
                try {
                    byte[] data = new byte[buf.readableBytes()];
                    buf.readBytes(data);
                    fos.write(data);
                    receivedBytes += data.length;
                    
                    // 显示传输进度
                    if (receivedBytes % (1024 * 1024) == 0) {
                        double progress = (double) receivedBytes / totalBytes * 100;
                        System.out.printf("传输进度: %.1f%% (%d/%d bytes)\n", 
                            progress, receivedBytes, totalBytes);
                    }
                    
                } finally {
                    buf.release();
                }
            }
        }
        
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (fos != null) {
                fos.close();
                System.out.println("文件接收完成，总共接收: " + receivedBytes + " bytes");
            }
            ctx.close();
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
    
    /**
     * 大文件分块传输演示
     */
    private static void chunkedFileTransferDemo() throws Exception {
        System.out.println("大文件分块零拷贝传输演示");
        
        // 创建大文件（50MB）
        String largeFile = "large_test_file.dat";
        createLargeFile(largeFile, 50 * 1024 * 1024);
        
        try {
            RandomAccessFile file = new RandomAccessFile(largeFile, "r");
            FileChannel fileChannel = file.getChannel();
            
            long fileSize = fileChannel.size();
            int chunkSize = 5 * 1024 * 1024; // 5MB每块
            int chunkCount = (int) Math.ceil((double) fileSize / chunkSize);
            
            System.out.println("文件大小: " + fileSize + " bytes");
            System.out.println("分块大小: " + chunkSize + " bytes");
            System.out.println("总块数: " + chunkCount);
            
            // 模拟分块传输
            for (int i = 0; i < chunkCount; i++) {
                long chunkStart = (long) i * chunkSize;
                long chunkLength = Math.min(chunkSize, fileSize - chunkStart);
                
                FileRegion chunkRegion = new DefaultFileRegion(
                    fileChannel, chunkStart, chunkLength);
                
                System.out.printf("传输第 %d 块: 偏移量=%d, 长度=%d bytes\n", 
                    i + 1, chunkStart, chunkLength);
                
                // 模拟传输（实际中会通过网络发送）
                long transferred = chunkRegion.transferTo(null, 0);
                System.out.println("本块传输字节: " + transferred);
                
                // 模拟网络延迟
                Thread.sleep(100);
            }
            
            fileChannel.close();
            file.close();
            
        } finally {
            new File(largeFile).delete();
        }
        
        System.out.println("分块传输演示完成");
    }
    
    /**
     * 创建大文件
     */
    private static void createLargeFile(String filename, long size) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            byte[] buffer = new byte[1024 * 1024]; // 1MB缓冲区
            long written = 0;
            
            while (written < size) {
                int toWrite = (int) Math.min(buffer.length, size - written);
                // 填充测试数据
                for (int i = 0; i < toWrite; i++) {
                    buffer[i] = (byte) ((written + i) % 256);
                }
                fos.write(buffer, 0, toWrite);
                written += toWrite;
            }
        }
        System.out.println("创建大文件: " + filename + " (" + size + " bytes)");
    }
    
    /**
     * 文件信息消息（用于传输文件元数据）
     */
    private static class FileInfoMessage {
        private final long fileSize;
        
        public FileInfoMessage(long fileSize) {
            this.fileSize = fileSize;
        }
        
        public long getFileSize() {
            return fileSize;
        }
    }
}