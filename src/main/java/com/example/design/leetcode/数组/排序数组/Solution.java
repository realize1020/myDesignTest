package com.example.design.leetcode.数组.排序数组;

import java.util.Arrays;

public class Solution {

    /**
     * 冒泡排序
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        boolean isSwap=true;
        for (int i = 0; i < arr.length - 1; i++) {
            if(!isSwap){
                break;
            }
            isSwap=false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 如果左边的数大于右边的数，则交换，保证右边的数字最大
                    swap(arr, j, j + 1);
                    isSwap=true;
                }
            }
        }
    }
    // 交换元素
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    /**
     * 插入排序
     * @param arr
     */
    public static void insertSort(int[] arr) {
        // 从第二个数开始，往前插入数字
        for (int i = 1; i < arr.length; i++) {
            // j 记录当前数字下标
            // 当前数字比前一个数字小，则将当前数字与前一个数字交换
//            int j = i;
//            while (j >= 1 && arr[j] < arr[j - 1]) {
//                swap(arr, j, j - 1);
//                // 更新当前数字下标
//                j--;
//            }

            for(int j=i;j>0;j--){
                if(arr[j]<arr[j-1]){
                    swap(arr, j, j - 1);
                }
            }
        }
    }

    /**
     * 希尔排序
     * @param arr
     */
    public static void shellSort(int[] arr) {
        for(int gap = arr.length/2;gap>0;gap/=2){
            // 从第二个数开始，往前插入数字
            for (int i = 0; i < gap; i++) {

                for(int j=i+gap;j<arr.length;j=j+gap){
                    // currentNumber 站起来，开始找位置
                    int currentNumber = arr[j];
                    int preIndex = j - gap;
                    while(preIndex>=i && currentNumber<arr[preIndex]){
                        // 向后挪位置
                        arr[preIndex + gap] = arr[preIndex];
                        preIndex -= gap;
                    }
                    // currentNumber 找到了自己的位置，坐下
                    arr[preIndex + gap] = currentNumber;
                }
            }
        }

    }


    public static void shellSort2(int[] arr) {
        for(int gap = arr.length/2;gap>0;gap/=2){
            // 从第二个数开始，往前插入数字
            for (int i = 0; i < gap; i++) {

                for(int j=i+gap;j<arr.length;j=j+gap){
                    // currentNumber 站起来，开始找位置
                    int currentNumber = arr[j];
                    int preIndex = j - gap;
                    while(preIndex>=i && currentNumber<arr[preIndex]){
                        // 向后挪位置
                        swap(arr,preIndex + gap,preIndex);
                        preIndex -= gap;
                    }
                    // currentNumber 找到了自己的位置，坐下
                    //arr[preIndex + gap] = currentNumber;
                }
            }
        }

    }


    /**
     * 改进后的插入排序
     * @param arr
     */
    public static void insertSort2(int[] arr) {
        // 从第二个数开始，往前插入数字
        for (int i = 1; i < arr.length; i++) {
            int currentNumber = arr[i];
            int j = i - 1;
            // 寻找插入位置的过程中，不断地将比 currentNumber 大的数字向后挪
            while (j >= 0 && currentNumber < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 两种情况会跳出循环：1. 遇到一个小于或等于 currentNumber 的数字，跳出循环，currentNumber 就坐到它后面。
            // 2. 已经走到数列头部，仍然没有遇到小于或等于 currentNumber 的数字，也会跳出循环，此时 j 等于 -1，currentNumber 就坐到数列头部。
            arr[j + 1] = currentNumber;
        }
    }

    /**
     * 选择排序
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int mid=i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[mid] > arr[j]) {
                    mid=j;
                    // 将最小元素交换至首位
                    int temp = arr[i];
                    arr[i] = arr[mid];
                    arr[mid] = temp;
                }
            }

        }
    }

    /**
     * 快速排序
     * @param arr
     */
    public static void quickSort(int[] arr){
        quickSort(arr,0,arr.length-1);
    }

    private static void quickSort(int[] arr, int start, int end) {
        if(start<end){
            int mid=partition(arr,start,end);
            quickSort(arr,start,mid-1);
            quickSort(arr,mid+1,end);
        }

    }

    private static int partition(int[] arr, int start, int end) {
        int pivot = arr[start];
        while(start<end){
            while(start<end && arr[end]>=pivot){
                end--;
            }
            arr[start]=arr[end];
            while(start<end && arr[start]<=pivot){
                start++;
            }
            arr[end]=arr[start];
        }
        arr[start] = pivot;
        return start;
    }

    static void quickSort2(int a[]){
        quickSort2(a,0,a.length-1);
    }

    //快速排序算法
    static void quickSort2(int a[],int low,int high)
    {
        if(low<high)                                        //递归出口
        //Partition()是划分操作，将表a[low...high]划分为满足上述条件的两个子表
        {
            int pivotpos = Partition(a,low,high);           //划分
            quickSort2(a,low,pivotpos-1);                    //依次对两个字表进行递归排序
            quickSort2(a,pivotpos+1,high);
        }
    }
    static int Partition(int a[],int low,int high)
    {
        int pivot = a[low];                            //当前表中第一个元素设为枢轴质，对表进行划分
        while(low<high)                                     //循环跳出条件
        {
            while(low<high && a[high]>=pivot)
                --high;
            a[low] = a[high];                               //将比枢轴值小的元素移动到左端
            while(low<high && a[low]<=pivot)
                ++low;
            a[high] = a[low];                               //将比枢轴值大的元素移动到右端
        }
        a[low] = pivot;                                     //枢轴元素存放到最终位置
        return low;                                         //返回存放枢轴的最终位置
    }






    public static int[] sortArray(int[] nums) {
        quickSort2(nums);
        return nums;
    }


    public static void main(String[] args) {
        int[] arr = {6,2,1,3,5,4};
        sortArray(arr);
        System.out.println(Arrays.toString(arr));
    }
}
