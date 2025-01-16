package com.example.design.leetcode.堆;

import java.util.Arrays;

/**
 * 堆排序是一种比较排序算法，它的时间复杂度为 O(N log N)，其中 N 是数组的长度。您的实现中包含了一个优化：
 * 直接从数组构建最大堆，而不是逐个插入元素。这一步骤的时间复杂度是 O(N)，比逐个插入（每个插入操作 O(log N)）更高效。
 */
public class HeapSort {

    // 堆排序额外空间复杂度O(1)
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // O(N*logN)
//		for (int i = 0; i < arr.length; i++) { // O(N)
//			heapInsert(arr, i); // O(logN)
//		}
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        // O(N*logN)
        while (heapSize > 0) { // O(N)
            heapify(arr, 0, heapSize); // O(logN)
            swap(arr, 0, --heapSize); // O(1)
        }
    }

    // arr[index]刚来的数，往上
    public static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    // arr[index]位置的数，能否往下移动
    public static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1; // 左孩子的下标
        while (left < heapSize) { // 下方还有孩子的时候
            // 两个孩子中，谁的值大，把下标给largest
            // 1）只有左孩子，left -> largest
            // 2) 同时有左孩子和右孩子，右孩子的值<= 左孩子的值，left -> largest
            // 3) 同时有左孩子和右孩子并且右孩子的值> 左孩子的值， right -> largest
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            // 父和较大的孩子之间，谁的值大，把下标给largest
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
        int [] arr={2,3,1,5,10,4,9};
        heapSort2(arr);
        System.out.println(Arrays.toString(arr));
    }



    public static void heapSort2(int[] arr){
        if(arr.length==0||arr==null){
            return;
        }
        for(int i=arr.length/2-1;i>=0;i--){
            heapify2(arr,i,arr.length-1);
        }
        //对第一个排好的堆顶元素进行排除(和最后一个元素交换位置)，然后剩下的重新下浮调整堆。
        swap(arr,0,arr.length-1);
        int j=0;
        int end=arr.length-2;
        while(j<=end){
            heapify2(arr,j,end);
            swap(arr,0,end);
            end--;
        }
    }

    private static void heapify2(int[] arr, int index, int size) {
        while(true){
            int left = index*2+1;
            int right = index*2+2;
            int largest = index;
            if(left<=size&&arr[index]<arr[left]){
                swap(arr,index,left);
                largest=left;
            }
            if(right<=size&&arr[index]<arr[right]){
                swap(arr,index,right);
                largest=right;
            }

            if(largest==index){
                break;
            }else{
                index=largest;
            }
        }
    }
}
