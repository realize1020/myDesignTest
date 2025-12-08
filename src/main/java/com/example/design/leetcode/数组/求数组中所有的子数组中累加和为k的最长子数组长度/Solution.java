package com.example.design.leetcode.数组.求数组中所有的子数组中累加和为k的最长子数组长度;

import java.util.HashMap;

/**
 * 给定一个无序数组 ar，其中元素可正、可负、可0，给定一个整数 k。求 arr 所有的子数组中累加和为k的最长子数组长度。
 */
public class Solution {

    public static int getMaxLength(int [] arr,int k ){
        int len = 0;
        int left= 0;
        int sum = arr[0];
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(0,-1);//这里默认值给什么
        while(left<arr.length){
            if(map.containsKey(sum-k)){
                len = Math.max(left-map.get(sum - k),len);
            }
            if(!map.containsKey(sum)){
                map.put(sum,left);
            }
            left++;
            if(left==arr.length){
                break;
            }
            sum += arr[left];
        }
        return len;
    }

    /**
     * ai
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // 重要：空前缀和为0，索引为-1

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];

            // 如果找到了sum-k，说明存在子数组和为k
            if (map.containsKey(sum - k)) {
                int len = i - map.get(sum - k);
                maxLen = Math.max(maxLen, len);
            }

            // 只有第一次出现的前缀和才记录（保证最长）
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }

        return maxLen;
    }

    /**
     * 这种不行！
     * 主要问题 ：
     * 1. 1.
     *    preSum 更新逻辑错误 ：不应该在找到匹配时就更新
     * 2. 2.
     *    长度计算错误 ： maxLen = Math.max(maxLen, index) 应该计算子数组长度
     * 3. 3.
     *    查找逻辑错误 ：只检查了相邻的前缀和差
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength3(int[] arr, int k){
        int[] nums = buildPrefixSumArray(arr);
        int maxLen = 0;
        int preSum=0;
        int index=0;
        for(int i=0;i<nums.length;i++){
            int num = nums[i];
            if((num-preSum)==k){
                index = i;
                maxLen = Math.max(maxLen, index);
                preSum = num;
            }
        }
        return maxLen;
    }

    /**
     * 双重循环（暴力优化）
     * @param arr
     * @param k
     * @return
     */
    public static int getMaxLength4(int[] arr, int k) {
        if (arr == null || arr.length == 0) return 0;

        int[] prefixSum = buildPrefixSumArray(arr);
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j <= i; j++) {
                // 计算子数组 arr[j...i] 的和
                int sum = prefixSum[i] - (j > 0 ? prefixSum[j - 1] : 0);
                if (sum == k) {
                    maxLen = Math.max(maxLen, i - j + 1);
                }
            }
        }

        return maxLen;
    }


    /**
     * 构建前缀和数组
     * @param arr
     * @return
     */
    public static int[] buildPrefixSumArray(int[] arr) {
        int[] prefixSum = new int[arr.length];
        prefixSum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + arr[i];
        }
        return prefixSum;
    }



    public static void main(String[] args) {
//        int [] arr = {1,0,-3,4,5};
//        int k=1;
        int[] arr = {1, 2, 3, -3, 1, 1, 1, 4, 2, -3};
        int k = 3;
        System.out.println(getMaxLength3(arr,k));
    }
}
