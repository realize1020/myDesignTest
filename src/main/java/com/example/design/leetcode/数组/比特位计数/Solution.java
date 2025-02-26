package com.example.design.leetcode.数组.比特位计数;

import java.util.Arrays;

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
