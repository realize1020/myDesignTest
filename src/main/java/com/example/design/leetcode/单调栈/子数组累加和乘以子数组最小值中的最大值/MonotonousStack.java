package com.example.design.leetcode.单调栈.子数组累加和乘以子数组最小值中的最大值;

import java.util.Arrays;
import java.util.Stack;

/**
 * 题目
 * 给定一个只包含正数的数组arr，arr中任何一个子数组sub，
 * 一定都可以算出(sub累加和) * (sub中的最小值)是什么，那么所有子数组中，这个值最大是多少?
 */
public class MonotonousStack {
    // arr = [ 3, 1, 2, 3]
    //         0  1  2  3
    //  [
    //     0 : [-1,  1]
    //     1 : [-1, -1]
    //     2 : [ 1, -1]
    //     3 : [ 2, -1]
    //  ]

    /**
     * 左程云：求出数组中每个元素自己作为子数组最小值的左右最小边界。
     * @param arr
     * @return
     */
    public static int[][] getNearLessNoRepeat(int[] arr) {
        int[][] res = new int[arr.length][2];
        // 只存位置！
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) { // 当遍历到i位置的数，arr[i]
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int j = stack.pop();
                int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
                res[j][0] = leftLessIndex;
                res[j][1] = i;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
            res[j][0] = leftLessIndex;
            res[j][1] = -1;
        }
        return res;
    }

    //暴力方法，时间复杂度O(n^3)
    public static int max1(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int minNum = Integer.MAX_VALUE;
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += arr[k];
                    minNum = Math.min(minNum, arr[k]);
                }
                max = Math.max(max, minNum * sum);
            }
        }
        return max;
    }



    /**
     * 豆包 算出(sub累加和) * (sub中的最小值)是什么，那么所有子数组中，这个值最大是多少
     * @param arr
     * @return
     */
    public static int maxValue(int[] arr) {
        // 计算前缀和数组，用于快速计算子数组的累加和
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        int max = Integer.MIN_VALUE;
        // 单调栈，用于找到每个元素左右第一个比它小的元素的位置
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                int j = stack.pop();
                // 计算以 arr[j] 为最小值的子数组的累加和
                int left = stack.isEmpty() ? -1 : stack.peek();
                int curSum = left == -1 ? sum[i - 1] : sum[i - 1] - sum[left];
                // 计算 (sub累加和) * (sub中的最小值)
                max = Math.max(max, curSum * arr[j]);
            }
            stack.push(i);
        }
        // 处理栈中剩余的元素
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek();
            int curSum = left == -1 ? sum[arr.length - 1] : sum[arr.length - 1] - sum[left];
            max = Math.max(max, curSum * arr[j]);
        }
        return max;
    }

    public static void main(String[] args) {
//        int[] arr = {3, 1, 2, 4, 3};
//        int[][] result = getNearLessNoRepeat(arr);
//        for (int i = 0; i < result.length; i++) {
//            System.out.println("元素 " + arr[i] + " 的左小值索引: " + result[i][0] + ", 右小值索引: " + result[i][1]);
//        }


        int[] arr = {3, 1, 2, 4, 3}; //前缀和[3,4,6,10,13]
        System.out.println(maxValue(arr));
    }
}
