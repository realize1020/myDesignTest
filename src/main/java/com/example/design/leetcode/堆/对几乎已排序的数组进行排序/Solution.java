package com.example.design.leetcode.堆.对几乎已排序的数组进行排序;

import java.util.PriorityQueue;

public class Solution {
    /**
     * 对几乎已排序的数组进行排序，每个元素的位置最多偏离其最终排序位置不超过 k 个位置。
     *
     * @param arr 输入数组
     * @param k   每个元素的最大偏移量
     */
    public static void sortedArrDistanceLessK(int[] arr, int k) {
        if (arr == null || arr.length <= 1 || k <= 0) {
            return;
        }

        // 默认小根堆
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // 填充初始窗口 [0, k-1]
        for (int i = 0; i < Math.min(k, arr.length); i++) {
            minHeap.add(arr[i]);
        }

        // 处理剩余元素
        int index = 0;
        for (int i = k; i < arr.length; i++) {
            arr[index++] = minHeap.poll();
            minHeap.add(arr[i]);
        }

        // 处理最后的窗口
        while (!minHeap.isEmpty()) {
            arr[index++] = minHeap.poll();
        }
    }

    // 测试用例
    public static void main(String[] args) {
        int[] arr = {6, 5, 3, 2, 8, 10, 9};
        int k = 3;
        sortedArrDistanceLessK(arr, k+1);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
