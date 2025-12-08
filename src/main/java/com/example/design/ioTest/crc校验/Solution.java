package com.example.design.ioTest.crc校验;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class Solution {
    public static void main(String[] args) {
        String str = "hello world";
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        System.out.println("crc32加密后："+crc32.getValue());
        long entcy = (int)crc32.getValue();
        //使用了ByteBuffer进行优化
        ByteBuffer buffer = ByteBuffer.allocate(str.getBytes().length+2);
        buffer.putShort((short)str.getBytes().length);
        buffer.putInt((int)crc32.getValue());

        ByteBuffer buffer2 = ByteBuffer.wrap(str.getBytes());
        byte[] body =new byte[str.getBytes().length];
        buffer2.get(body);
        crc32.reset();
        crc32.update(body);
        if((int)crc32.getValue()!=entcy){
            System.out.println("消息Body CRC校验失败");
        }else{
            System.out.println("消息Body CRC校验成功");
        }
    }
}
