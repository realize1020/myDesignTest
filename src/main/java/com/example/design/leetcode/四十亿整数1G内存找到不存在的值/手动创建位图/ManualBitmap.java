package com.example.design.leetcode.四十亿整数1G内存找到不存在的值.手动创建位图;

import com.example.design.leetcode.四十亿整数1G内存找到不存在的值.BitmapSolution;

public class ManualBitmap {
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private static final int ARRAY_SIZE = MAX_VALUE / 8 + 1; // 向上取整
    private final byte[] bitArr;
    
    public ManualBitmap() {
        this.bitArr = new byte[ARRAY_SIZE];
    }
    
    public void setBit(int number) {
        if (number < 0 || number > MAX_VALUE) return;
        
        int byteIndex = number / 8;
        int bitOffset = number % 8;
        bitArr[byteIndex] |= (1 << bitOffset);
    }
    
    public boolean getBit(int number) {
        if (number < 0 || number > MAX_VALUE) return false;
        
        int byteIndex = number / 8;
        int bitOffset = number % 8;
        return (bitArr[byteIndex] & (1 << bitOffset)) != 0;
    }

    public void printMemoryUsage() {
        long memoryBytes = bitArr.length;
        System.out.printf("位图内存占用: %d bytes (%.2f MB)%n",
                         memoryBytes, memoryBytes / 1024.0 / 1024.0);
    }


    public static void main(String[] args) {
        ManualBitmap solution = new ManualBitmap();
        for(int i = 0; i < 2000000000; i++){
            solution.setBit( i);
        }

        solution.printMemoryUsage();
    }
}