package com.example.design.ioTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
        //测试内存分配
        //testAllocate();

        //其他测试
        //anotherTest();

        //slice方法测试
        //sliceTest();

        //flip方法测试
        //flipTest();

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        // 写入文本数据
        String text = "Hello";
        buffer.put(text.getBytes("UTF-8"));

        byte[] data = new byte[5];
        buffer.get(data);

        //不 flip() 直接读取
        //输出为空数据
        System.out.println("不 flip() 直接读取文本内容: " + new String(data, "UTF-8"));


        // flip() 后读取
        buffer.flip();
        byte[] data2 = new byte[5];
        buffer.get(data2);

        // 显示文本
        System.out.println("flip() 后读取文本内容: " + new String(data2, "UTF-8"));  // 输出: Hello

    }

    private static void flipTest() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        // 写入数据
        buffer.putInt(123);
        buffer.putShort((short) 456);
        // 此时：position=6, limit=1024, capacity=1024

        // 不 flip() 直接读取
        byte[] data1 = new byte[10];
        buffer.get(data1);  // 从 position=6 开始读取，读到 limit=1024
        // 结果：读取了 1018 字节的垃圾数据
        int intValue = buffer.getInt();
        short shortValue = buffer.getShort();
        System.out.println("int值: " + intValue);
        System.out.println("short值: " + shortValue);

        // 重置后 flip() 再读取
        buffer.flip();  // position=0, limit=6
        // 按原始类型读取
        int intValue2 = buffer.getInt();
        short shortValue2 = buffer.getShort();
        // 结果：正确读取了 6 字节的有效数据
        System.out.println("int值: " + intValue2);      // 输出: 123
        System.out.println("short值: " + shortValue2);  // 输出: 456
    }

    private static void sliceTest() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }
        //现在我们对这个缓冲区 分片，以创建一个包含槽 3 到槽 6 的子缓冲区。在某种意义上，子缓冲区就像原来的缓冲区中的一个 窗口 。
        //窗口的起始和结束位置通过设置 position 和 limit 值来指定，然后调用 Buffer 的 slice() 方法：
        //片 是缓冲区的 子缓冲区 。不过，片段 和 缓冲区 共享同一个底层数据数组。
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();

        //遍历子缓冲区，将每一个元素乘以 11 来改变它。例如，5 会变成 55。
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 11;
            slice.put(i, b);
        }

        //原缓冲区中的内容也发生了改变：

        buffer.position( 0 );
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }

    private static void anotherTest() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("--------Test reset----------");
        buffer.clear();
        buffer.position(5);
        buffer.mark();
        buffer.position(10);
        System.out.println("before reset:" + buffer);
        buffer.reset();
        System.out.println("after reset:" + buffer);

        System.out.println("--------Test rewind--------");
        buffer.clear();
        buffer.position(10);
        buffer.limit(15);
        System.out.println("before rewind:" + buffer);
        buffer.rewind();
        System.out.println("before rewind:" + buffer);

        System.out.println("--------Test compact--------");
        buffer.clear();
        buffer.put("abcd".getBytes());
        System.out.println("before compact:" + buffer);
        System.out.println(new String(buffer.array()));
        buffer.flip();
        System.out.println("after flip:" + buffer);
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println("after three gets:" + buffer);
        System.out.println("\t" + new String(buffer.array()));
        buffer.compact();
        System.out.println("after compact:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("------Test get-------------");
        buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
                .put((byte) 'e').put((byte) 'f');
        System.out.println("before flip()" + buffer);
        // 转换为读取模式
        buffer.flip();
        System.out.println("before get():" + buffer);
        System.out.println((char) buffer.get());
        System.out.println("after get():" + buffer);
        // get(index)不影响position的值
        System.out.println((char) buffer.get(2));
        System.out.println("after get(index):" + buffer);
        byte[] dst = new byte[10];
        buffer.get(dst, 0, 2);
        System.out.println("after get(dst, 0, 2):" + buffer);
        System.out.println("\t dst:" + new String(dst));
        System.out.println("buffer now is:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("--------Test put-------");
        ByteBuffer bb = ByteBuffer.allocate(32);
        System.out.println("before put(byte):" + bb);
        System.out.println("after put(byte):" + bb.put((byte) 'z'));
        System.out.println("\t" + bb.put(2, (byte) 'c'));
        // put(2,(byte) 'c')不改变position的位置
        System.out.println("after put(2,(byte) 'c'):" + bb);
        System.out.println("\t" + new String(bb.array()));
        // 这里的buffer是 abcdef[pos=3 lim=6 cap=32]
        bb.put(buffer);
        System.out.println("after put(buffer):" + bb);
        System.out.println("\t" + new String(bb.array()));
    }

    private static void testAllocate() {
        System.out.println("----------Test allocate--------");
        System.out.println("before alocate:"
                + Runtime.getRuntime().freeMemory());

        // 如果分配的内存过小，调用Runtime.getRuntime().freeMemory()大小不会变化？
        // 要超过多少内存大小JVM才能感觉到？
        ByteBuffer buffer = ByteBuffer.allocate(102400);
        System.out.println("buffer = " + buffer);

        System.out.println("after alocate:"
                + Runtime.getRuntime().freeMemory());

        // 这部分直接用的系统内存，所以对JVM的内存没有影响
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(102400);
        System.out.println("directBuffer = " + directBuffer);
        System.out.println("after direct alocate:"
                + Runtime.getRuntime().freeMemory());

        System.out.println("----------Test wrap--------");
        byte[] bytes = new byte[32];
        buffer = ByteBuffer.wrap(bytes);
        System.out.println(buffer);

        buffer = ByteBuffer.wrap(bytes, 10, 10);
        System.out.println(buffer);
    }
}
