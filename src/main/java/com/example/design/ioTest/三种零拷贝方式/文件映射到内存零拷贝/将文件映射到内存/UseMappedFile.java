package com.example.design.ioTest.三种零拷贝方式.文件映射到内存零拷贝.将文件映射到内存;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class UseMappedFile {
    private static final int start = 0;
    private static final int size = 1024;

    public static void main(String args[]) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("D:\\work\\问题.txt", "rw");
        FileChannel fc = raf.getChannel();

        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

//        mbb.put(0, (byte)97);//将第0个字节设为97（字符'a'）
//        mbb.put(10, (byte)122);//第十个字节换成字符z

        // 使用UTF-8编码写入字符
        byte[] aBytes = "a".getBytes(StandardCharsets.UTF_8);
        byte[] zBytes = "z".getBytes(StandardCharsets.UTF_8);

        // 确保有足够的空间写入UTF-8编码的字符
        mbb.put(0, aBytes[0]);  // 写入字符'a'
        mbb.put(10, zBytes[0]); // 写入字符'z' (ASCII字符在UTF-8中占1个字节)

        raf.close();
    }
}