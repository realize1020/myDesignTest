package com.example.design.ioTest.传统传输文件方式;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileInputStream;

public class TestChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        traditionalTransfer(ctx.channel(),"test.txt",0,100);
        super.channelRead(ctx, msg);
    }



    //传统方式的文件传输
    public void traditionalTransfer(Channel channel, String fileName, long offset, int size){
        try {
            //分配堆内存缓冲区
            byte[] buffer = new byte[size];
            //从文件读取数据到缓冲区
            FileInputStream fis =new FileInputStream(fileName);
            fis.skip(offset);
            fis.read (buffer);
            fis.close();
            //将缓冲区数据写入网络通道
            //将缓冲区数据写入网络通道
            channel.writeAndFlush(Unpooled.wrappedBuffer (buffer));
        } catch(Exception e){
            System.out.println("传统文件传输失败");
        }
    }
    }

