package com.example.design.leetcode.数组.归并排序;

public class Solution {
    public static void mergeSort1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    // 对数组 arr 在 [L...R] 范围上进行排序
    public static void process(int[] arr, int L, int R) {
        if (L == R) {
            return;
        }
        // 防止(L + R)溢出，使用位运算计算中间点
        int mid = L + ((R - L) >> 1);
        process(arr, L, mid);          // 排序左半部分
        process(arr, mid + 1, R);      // 排序右半部分
        merge(arr, L, mid, R);         // 合并两个有序部分
    }

    // 将 arr[L...M] 和 arr[M+1...R] 两个有序区间合并成一个有序区间 arr[L...R]
    public static void merge(int[] arr, int L, int M, int R) {
        int[] help = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = M + 1;

        // 比较左右两部分的数据，按顺序放入 help 数组中
        while (p1 <= M && p2 <= R) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }

        // 如果左边还有剩余数据，直接复制到 help 中
        while (p1 <= M) {
            help[i++] = arr[p1++];
        }

        // 如果右边还有剩余数据，直接复制到 help 中
        while (p2 <= R) {
            help[i++] = arr[p2++];
        }

        // 将 help 数组中的数据复制回原数组 arr 中
        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
    }

    public static void main(String[] args) {
        int [] arr = {3,5,1,7,10,2,4};
        mergeSort1(arr);
    }


}
