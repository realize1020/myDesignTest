package com.example.design.leetcode.数组.最大子数组和;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 子数组
 * 是数组中的一个连续部分。
 * 示例 1：
 *
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 * 示例 2：
 *
 * 输入：nums = [1]
 * 输出：1
 * 示例 3：
 *
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23
 */
public class Solution {
    public static void main(String[] args) {
       int [] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int i = maxSubArray(nums);
        System.out.println(i);

    }

    /**
     * 自己写的,不太对，maxSubArray3是根据官方修改。
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        int[] max = new int[nums.length];
        int maxValue =nums[0];
        max[0]=maxValue;
        for(int i=1;i<nums.length;i++){ //-2,1,-3,4,-1,2,1,-5,4
            maxValue+=nums[i];
            maxValue = Math.max(nums[i],maxValue);
            max[i]=maxValue;
        }
        System.out.println(Arrays.toString(max));
        return Arrays.stream(max).boxed().max(Comparator.comparingInt(Integer::intValue)).get();
    }

    public static int maxSubArray3(int[] nums) {
        //int[] max = new int[nums.length];
        int maxValue =nums[0];
        int pre = 0;
        for(int i=0;i<nums.length;i++){ //-2,1,-3,4,-1,2,1,-5,4
            pre+=nums[i];
            pre = Math.max(pre,nums[i]);
            //   max[i]=maxValue;
            maxValue = Math.max(pre,maxValue);
        }
        return maxValue;
//        System.out.println(Arrays.toString(max));
//        return Arrays.stream(max).boxed().max(Comparator.comparingInt(Integer::intValue)).get();
    }


    /**
     * 官方写法
     * @param nums
     * @return
     */
    public static int maxSubArray2(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }

}
