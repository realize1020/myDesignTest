package com.example.design.leetcode.四十亿整数1G内存找到不存在的值;

import java.util.BitSet;

public class BitmapSolution {
    private static final int MAX_VALUE = 2000000000; // 2^32 - 1
    private final BitSet bitSet;
    
    public BitmapSolution() {
        // BitSet会自动扩展，但我们可以预先设置大小
        this.bitSet = new BitSet(MAX_VALUE + 1);
    }
    
    public void markNumber(int number) {
        if (number >= 0 && number <= MAX_VALUE) {
            bitSet.set(number);
        }
    }
    
    public boolean isNumberPresent(int number) {
        return number >= 0 && number <= MAX_VALUE && bitSet.get(number);
    }
    
    public void printMemoryUsage() {
        long memoryBytes = bitSet.size() / 8;
        System.out.printf("位图内存占用: %d bytes (%.2f MB)%n", 
                         memoryBytes, memoryBytes / 1024.0 / 1024.0);
    }

    public static void main(String[] args) {
        BitmapSolution solution = new BitmapSolution();
        for(int i = 0; i < 2000000000; i++){
            solution.markNumber( i);
        }

        solution.printMemoryUsage();
    }
}