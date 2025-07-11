package com.example.design.leetcode.二叉树.树状数组.前缀和数组;

import java.util.Arrays;

/**
 *IndexTree前置，前缀和
 * 但是如果频繁改变原数组的值，就需要不断构建新的前缀和数组，所以一般使用树状数组
 */
public class HelpArray {

    public static void main(String[] args) {
        int [] arr = {1,2,3,4,5,6,7,8,9,10};
        int [] help =new int[arr.length];
        int sum = 0;
        for(int i=0;i<arr.length;i++){
            sum+=arr[i];
            help[i] = sum;
        }
        //打印前缀和
        System.out.println(Arrays.toString(help));

        //返回下标4到7的和
        System.out.println(help[7]-help[3]);
    }

}
