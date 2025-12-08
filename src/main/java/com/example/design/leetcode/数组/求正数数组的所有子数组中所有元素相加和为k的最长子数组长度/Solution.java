package com.example.design.leetcode.数组.求正数数组的所有子数组中所有元素相加和为k的最长子数组长度;

import java.util.Arrays;
/**
 * 【题目】
 * 给定一个数组 arr，该数组无序，但每个值均为正数，再给定一个正数k。求arr 的所有子数组中所有元素相加和为k的最长子数组长度。
 * 例如，arr=[1,2,1,1,1]，k=3。
 * 累加和为3的最长子数组为[1,1,1]，所以结果返回3。
 */
public class Solution {
    /**
     * 算法的时间复杂度是 O(n²)
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength(int [] arr,int k ) {
        int len = 0;
        int left= 0;
        int right = 0;
        while(left<arr.length){
            int sum = 0;
            for(int i=left;i<right;i++){
                sum+=arr[i];
            }
             if(sum<k){
                 if(right<arr.length){
                     right++;
                 }else{
                     left++;
                 }
             }else if(sum>k){
                 left++;
             }else{
                 len=Math.max(len,right-left);
                 left++;
             }
        }
        return len;
    }

    /**
     * 手搓出来。时间复杂度为 O(n)，空间复杂度为 O(1)
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength2(int [] arr,int k ) {
        int len = 0;
        int left= 0;
        int right = 0;
        int sum = arr[right];
        while(left<arr.length){
            if(sum==k){
                len=Math.max(len,right-left+1);
                sum-=arr[left];// sum-=arr[left++];
                left++;
                continue;
            }
            if(sum<k){
                right++;
                if(right==arr.length){
                    break;
                }
                sum+=arr[right];
            }else{
                sum-=arr[left];
                left++;
            }

        }
        return len;
    }


    /**
     * getMaxLength2的写法优化
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength3(int [] arr,int k ) {
        int len = 0;
        int left= 0;
        int right = 0;
        int sum = arr[right];
        while(left<arr.length){
            if(sum<k){
                right++;
                if(right==arr.length){
                    break;
                }
                sum+=arr[right];
            }else if(sum>k){
                sum-=arr[left];
                left++;
            }else{
                len=Math.max(len,right-left+1);
                sum-=arr[left];// sum-=arr[left++];
                left++;
            }

        }
        return len;
    }

    /**
     * 标准版写法
     * @param arr
     * @param K
     * @return
     */
    public static int getMaxLength4(int[] arr, int K) {
        if (arr == null || arr.length == 0 || K <= 0) {
            return 0;
        }
        int left = 0;
        int right = 0;
        int sum = arr[0];
        int len = 0;
        while (right < arr.length) {
            if (sum == K) {
                len = Math.max(len, right - left + 1);
                sum -= arr[left++];
            } else if (sum < K) {
                right++;
                if (right == arr.length) {
                    break;
                }
                sum += arr[right];
            } else {
                sum -= arr[left++];
            }
        }
        return len;
    }


    /**
     * 修改int sum = arr[0];为int sum = 0，然后重新修改循环里的逻辑，这种方法仍然保持 O(n) 时间复杂度，并且逻辑更加清晰易懂。
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLengthAlternative(int[] arr, int k) {
        int len = 0;
        int left = 0;
        int right = 0;
        int sum = 0; // 初始化为0

        while (left < arr.length) {
            // 动态维护sum值，确保它表示当前窗口[left, right)的和
            while (right < arr.length && sum < k) {
                sum += arr[right];
                right++;
            }

            if (sum == k) {
                len = Math.max(len, right - left);
                sum -= arr[left];
                left++;
            } else if (sum > k) {
                sum -= arr[left];
                left++;
            } else {
                // sum < k 但right已到末尾
                break;
            }
        }
        return len;
    }



    public static int right(int[] arr, int K) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (valid(arr, i, j, K)) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    // for test
    public static boolean valid(int[] arr, int L, int R, int K) {
        int sum = 0;
        for (int i = L; i <= R; i++) {
            sum += arr[i];
        }
        return sum == K;
    }

    public static int[] generatePositiveArray(int size, int value) {
        int[] ans = new int[size];
        for (int i = 0; i != size; i++) {
            ans[i] = (int) (Math.random() * value) + 1;
        }
        return ans;
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i != arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {

        int [] arr2 = {1,2,3,4,5};
        int k=9;
        System.out.println(getMaxLengthAlternative(arr2,k));


//        int len = 50;
//        int value = 100;
//        int testTime = 500000;
//        System.out.println("test begin");
//        for (int i = 0; i < testTime; i++) {
//            int[] arr = generatePositiveArray(len, value);
//            int K = (int) (Math.random() * value) + 1;
//            int ans1 = getMaxLength3(arr, K);
//            int ans2 = right(arr, K);
//            if (ans1 != ans2) {
//                System.out.println("Oops!");
//                printArray(arr);
//                System.out.println("K : " + K);
//                System.out.println(ans1);
//                System.out.println(ans2);
//                break;
//            }
//        }
//        System.out.println("test end");


    }



}
