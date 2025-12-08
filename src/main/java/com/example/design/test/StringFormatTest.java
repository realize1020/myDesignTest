package com.example.design.test;

public class StringFormatTest {
        public static void main(String[] args) {
            // 模拟CommitLogManager中的文件名生成过程
            long[] testOffsets = {
                    0L,           // 第一个文件
                    1048576L,     // 第二个文件 (1MB)
                    2097152L,     // 第三个文件 (2MB)
                    123456789L,   // 较大的偏移量
                    999999999999999999L  // 非常大的偏移量
            };

            System.out.println("String.format(\"%020d\", newFileFromOffset) 格式化演示:");
            System.out.println("==================================================");

            for (long offset : testOffsets) {
                String fileName = String.format("%020d", offset);
                System.out.printf("原始偏移量: %d\n", offset);
                System.out.printf("格式化后: %s\n", fileName);
                System.out.printf("数值化文件名: %d\n", Long.parseLong(fileName));
                System.out.printf("数值化文件名: %d\n", Long.valueOf(fileName));
                System.out.printf("文件名长度: %d\n", fileName.length());
                System.out.println("---");
            }

            // 对比不同格式化方式
            System.out.println("\n不同格式化方式对比:");
            System.out.println("==================================================");

            long offset = 12345L;

            System.out.println("1. %020d (20位，0填充): " + String.format("%020d", offset));
            System.out.println("2. %20d (20位，空格填充): '" + String.format("%20d", offset) + "'");
            System.out.println("3. %d (原始格式): " + String.format("%d", offset));
            System.out.println("4. %10d (10位，空格填充): '" + String.format("%10d", offset) + "'");

            // 演示为什么需要20位
            System.out.println("\n为什么需要20位:");
            System.out.println("==================================================");

            // 计算最大可能的文件偏移量
            long maxFileSize = 1024L * 1024 * 1024 * 1024; // 1TB文件大小
            long maxFileCount = 1000000L; // 100万个文件
            long maxPossibleOffset = maxFileSize * maxFileCount;

            System.out.printf("假设单个文件最大: %d bytes (1TB)\\n", maxFileSize);
            System.out.printf("假设最多文件数: %d 个\\n", maxFileCount);
            System.out.printf("最大可能偏移量: %d\\n", maxPossibleOffset);
            System.out.printf("格式化后文件名: %s\\n", String.format("%020d", maxPossibleOffset));
            System.out.printf("需要的位数: %d 位\\n", String.valueOf(maxPossibleOffset).length());



            int a = 1;
            int b = 2;
            int c = 10;
            int d = 11;

            System.out.println(String.format("%02d", a));
            System.out.println(String.format("%02d", b));
            System.out.println(String.format("%02d", c));
            System.out.println(String.format("%02d", d));

        }
}
