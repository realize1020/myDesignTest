package com.example.design.ioTest;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class CharSetChannelSend {

    private static DatagramChannel channel = null;

    private static DatagramSocket socket = null;

    private static InetSocketAddress address = null;

    private static InetSocketAddress localAdress = null;

    public static void main(String[] args) throws IOException, InterruptedException {

        sender();

    }

    public static void sender() throws IOException, InterruptedException {

        channel = DatagramChannel.open();

        socket = channel.socket();

        localAdress = new InetSocketAddress(7000);

        address = new InetSocketAddress(InetAddress.getByName("localhost"), 8000);

        socket.bind(localAdress);

        socket.connect(InetAddress.getByName("localhost"), 8000);

        ByteBuffer br = ByteBuffer.allocate(1024);

        boolean flag = true;

        String info = "离离原上草，一岁一枯荣";

        br.clear();

        while (flag) {

            channel.send(ByteBuffer.wrap(info.getBytes()), address);

            System.out.println("数据已发送");

            Thread.sleep(3000);

        }

        socket.close();

    }

}
