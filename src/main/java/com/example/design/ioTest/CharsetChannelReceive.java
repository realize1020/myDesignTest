package com.example.design.ioTest;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

public class CharsetChannelReceive {

    private static DatagramChannel channel = null;

    private static DatagramSocket socket = null;

    private static InetSocketAddress address = null;

    public static void main(String[] args) throws IOException, InterruptedException {

        receive();

    }

    public static void receive() throws IOException, InterruptedException {

        channel = DatagramChannel.open();

        socket = channel.socket();

        address = new InetSocketAddress(8000);

        socket.bind(address);

        System.out.println("已连接服务器");

        ByteBuffer br = ByteBuffer.allocate(1024);

        boolean flag = true;

        while (flag) {

            channel.receive(br);

//            br.clear();
            // 设置正确的位置和限制
            br.flip();
            String info = Charset.forName("UTF-8").decode(br).toString();

            System.out.println(info);
            br.clear();
            Thread.sleep(3000);

        }

        socket.close();

    }

}
