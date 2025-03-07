package com.example.design.leetcode.数组.比特位计数;

import java.util.Arrays;

/**
 * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，
 * 计算其二进制数中的 1 的数目并将它们作为数组返回。
 * 输入: 2
 * 输出: [0,1,1]
 *
 * 输入: 5
 * 输出: [0,1,1,2,1,2]
 *
 * 示例假设 n = 29，其二进制表示为 11101：
 * 1. 初始状态：n = 11101
 * 2. 第一次操作：n & (n - 1)•n - 1 = 11100•n & (n - 1) = 11101 & 11100 = 11100 （最低位的1被清除了）
 * 3. 第二次操作：n & (n - 1)•n - 1 = 11011•n & (n - 1) = 11100 & 11011 = 11000 （最低位的1被清除了）
 * 4. 第三次操作：n & (n - 1)•n - 1 = 10111•n & (n - 1) = 11000 & 10111 = 10000 （最低位的1被清除了）
 * 5. 第四次操作：n & (n - 1)•n - 1 = 01111•n & (n - 1) = 10000 & 01111 = 00000 （最低位的1被清除了）
 * 经过4次操作后，n 变为0，说明 29 的二进制表示中有4个1。
 */
public class Solution {
    /**
     * 使用api
     * @param n
     * @return
     */
    public static int[] countBits(int n) {
        int [] arr =new int [n+1];
        for(int i=0;i<n+1;i++){
            int bitCount = Integer.bitCount(i);
            arr[i]=bitCount;
        }
        return arr;


    }

    /**
     * 使用n&n-1，一直到0，运算的次数就是1的数量
     * @param n
     * @return
     */
    public int[] countBits2(int n) {
        int[] bits = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            bits[i] = countOnes(i);
        }
        return bits;
    }

    public int countOnes(int x) {
        int ones = 0;
        while (x > 0) {
            x &= (x - 1);
            ones++;
        }
        return ones;
    }

    /**
     * 从暴力递归到动态规划：奇数则前一个偶数+1。偶数直接等于折半数,也就是只有一个1
     * @param num
     * @return
     */
    public static int[] countBits3(int num) {
        int[] result = new int[num + 1];
        for(int i = 1; i <= num; i++){
//            if ((i & 1) == 0){
//                result[i] = result[i >> 1];
//            }else {
//                result[i] = result[i - 1] + 1;
//            }
            //但是判断中又稍显复杂，再观察，其实能发现，其实奇数，也是向左移一位加上1，于是
            result[i] = result[i >> 1] + (i & 1);
        }
        return result;
    }


    public static void main(String[] args) {
        int[] bitcounts = countBits3(5);
        System.out.println(Arrays.toString(bitcounts));
    }
}
