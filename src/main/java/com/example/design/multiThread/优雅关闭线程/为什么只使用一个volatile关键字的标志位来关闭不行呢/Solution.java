package com.example.design.multiThread.优雅关闭线程.为什么只使用一个volatile关键字的标志位来关闭不行呢;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Solution {
    public static void main(String[] args) throws InterruptedException {
        //WorkerThread workerThread = new WorkerThread();
        WorkerThread2 workerThread = new WorkerThread2();
        workerThread.start();
        Thread.sleep(5000);
        workerThread.stopGracefully();
    }
}

/**
 * 只使用一个volatile关键字的标志位来关闭线程会有问题，有一些线程状态无法被检测到，比如线程在accept()阻塞，无法立即响应。
 */
class WorkerThread extends Thread {
    private volatile boolean running = true;

    @SneakyThrows
    @Override
    public void run() {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(3331));
        while (running) {
            try {
                System.out.println("开启服务");
                System.out.println("等待客户端建立连接");
                // 监听8080端口，获取客户端连接
                // 问题：如果线程在这里阻塞，无法检查running标志
                Socket socket = serverSocket.accept();// 阻塞操作
                System.out.println("建立连接："+socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopGracefully() {
        running = false; // 但线程在accept()阻塞，无法立即响应
        System.out.println("优雅的停止服务");
    }
}



class WorkerThread2 extends Thread {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private volatile boolean running = true;
    @Override
    public void run() {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(3331));
            System.out.println("开启服务");
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (running && !Thread.currentThread().isInterrupted()) {
                // 可中断的select操作
                System.out.println("等待客户端建立连接....");
                if (selector.select(1000) > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectedKeys.iterator();

                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        if (key.isAcceptable()) {
                            SocketChannel clientChannel = serverChannel.accept();
                            System.out.println("建立连接：" + clientChannel);
                        }
                        iter.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopGracefully() {
        running = false;
        this.interrupt();
        // 唤醒selector，使其从select()中返回
        if (selector != null) {
            selector.wakeup();
        }
    }
}