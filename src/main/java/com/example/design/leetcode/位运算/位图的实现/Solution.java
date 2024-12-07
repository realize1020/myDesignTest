package com.example.design.leetcode.位运算.位图的实现;

import java.util.HashSet;

public class Solution {

    // 这个类的实现是正确的
    public static class BitMap {

        private long[] bits;

        public BitMap(int max) {
            bits = new long[(max + 64) >> 6];
        }

        public void add(int num) { //num=1表示要把数组下表为1的数置为1：10，num=2表示要把下标为2的数置为1：100
            bits[num >> 6] |= (1L << (num & 63));//"|="或操作，之前位置为1的不变。然后新设置的位置为1
        }

        public void delete(int num) {
            bits[num >> 6] &= ~(1L << (num & 63));
        }

        /**
         * 这样写也行
         * @param num
         */
        public void delete2(int num) {
            bits[num >> 6] ^=(1L << (num & 63));
        }

        public boolean contains(int num) {
            return (bits[num >> 6] & (1L << (num & 63))) != 0;
        }

    }

    public static void main(String[] args) {
//        System.out.println("测试开始！");
//        int max = 10000;
//        BitMap bitMap = new BitMap(max);
//        HashSet<Integer> set = new HashSet<>();
//        int testTime = 10000000;
//        for (int i = 0; i < testTime; i++) {
//            int num = (int) (Math.random() * (max + 1));
//            double decide = Math.random();
//            if (decide < 0.333) {
//                bitMap.add(num);
//                set.add(num);
//            } else if (decide < 0.666) {
//                bitMap.delete(num);
//                set.remove(num);
//            } else {
//                if (bitMap.contains(num) != set.contains(num)) {
//                    System.out.println("Oops!");
//                    break;
//                }
//            }
//        }
//        for (int num = 0; num <= max; num++) {
//            if (bitMap.contains(num) != set.contains(num)) {
//                System.out.println("Oops!");
//            }
//        }
//        System.out.println("测试结束！");
//    }

        BitMap bitMap = new BitMap(128);
        bitMap.add(0);
        bitMap.add(1);
        bitMap.add(2);
        bitMap.add(3);
        bitMap.delete2(2);
        bitMap.delete2(1);
        boolean contains = bitMap.contains(1);
        System.out.println(contains);

    }
}
