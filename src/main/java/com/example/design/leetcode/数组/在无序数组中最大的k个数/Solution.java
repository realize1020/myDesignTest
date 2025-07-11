package com.example.design.leetcode.数组.在无序数组中最大的k个数;

import java.util.Arrays;

public class Solution {


    // 方法一，时间复杂度O(N*logN)
    // 排序+收集
    public static int[] maxTopK1(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        k = Math.min(N, k);
        Arrays.sort(arr);
        int[] ans = new int[k];
        for (int i = N - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }


    // 方法二，时间复杂度O(N + K*logN)
    // 解释：堆
    public static int[] maxTopK2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int N = arr.length;
        k = Math.min(N, k);
        // 从底向上建堆，时间复杂度O(N)
        for (int i = N - 1; i >= 0; i--) {
            heapify(arr, i, N);
        }
        // 只把前K个数放在arr末尾，然后收集，O(K*logN)
        //每次将堆顶元素（最大值）与堆的最后一个元素交换，堆大小减 1（排除已提取的最大值）。
        // 对新的堆顶（索引 0）调用 heapify，重新调整堆，确保新的堆顶是剩余元素的最大值。
        // 重复 K 次，数组末尾的 K 个元素（arr[N-K...N-1]）即为前 K 大元素（降序排列）。
        int heapSize = N;
        swap(arr, 0, --heapSize);
        int count = 1;
        while (heapSize > 0 && count < k) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
            count++;
        }
        int[] ans = new int[k];
        for (int i = N - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    public static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public static void main(String[] args) {
        int[] arr = {1,2,4,5,3,8,7,6,9,11,10,12,13,14,15};
        System.out.println(Arrays.toString(maxTopK2(arr, 3)));
    }


}
