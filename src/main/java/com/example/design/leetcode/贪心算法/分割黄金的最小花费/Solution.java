package com.example.design.leetcode.贪心算法.分割黄金的最小花费;

import java.util.PriorityQueue;

/**
 * 递归方法来解决一个优化问题，即如何将一组数值合并为一个单一的值，使得合并过程中产生的总成本最小。
 * 每个合并操作的成本是两个被合并数的和，并且每次合并会减少数组中的元素数量，直到只剩下一个元素。
 */
public class Solution {
    /**
     * 这是主方法，用于初始化合并过程并返回最终的最小成本。
     * @param arr
     * @return
     */
    public static int lessMoney1(int[] arr) {
        // 如果输入数组为空或长度为0，直接返回0，因为没有需要合并的元素
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 调用辅助方法 process 开始合并过程，初始成本为0
        return process(arr, 0);
    }

    /**
     * 这是一个递归方法，尝试所有可能的合并方案，并选择成本最小的一个。
     * @param arr
     * @param pre
     * @return
     */
    public static int process(int[] arr, int pre) {
        if (arr.length == 1) {// 如果数组中只剩下一个元素，则返回当前累积的成本
            return pre;
        }
        int ans = Integer.MAX_VALUE;// 初始化最小成本为最大整数值
        // 尝试所有可能的两两合并
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                // 计算当前合并的成本，并递归处理合并后的数组
                ans = Math.min(ans, process(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
            }
        }
        return ans;
    }

    public static int[] copyAndMergeTwo(int[] arr, int i, int j) {
        int[] ans = new int[arr.length - 1];// 新数组比原数组少一个元素
        int ansi = 0;
        // 复制除指定索引外的所有元素到新数组
        for (int arri = 0; arri < arr.length; arri++) {
            if (arri != i && arri != j) {
                ans[ansi++] = arr[arri];
            }
        }
        // 将指定索引位置的两个元素相加的结果放到新数组的最后一个位置
        ans[ansi] = arr[i] + arr[j];
        return ans;
    }

    /**
     * 使用优先级队列（小根堆）
     * @param arr
     * @return
     */
    public static int lessMoney2(int[] arr) {
        PriorityQueue<Integer> pQ = new PriorityQueue<>();
        for (int i = 0; i < arr.length; i++) {
            pQ.add(arr[i]);
        }
        int sum = 0;
        int cur = 0;
        while (pQ.size() > 1) {
            cur = pQ.poll() + pQ.poll();
            sum += cur;
            pQ.add(cur);
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] gold={20,30,10};
        System.out.println(lessMoney2(gold));
    }

}
