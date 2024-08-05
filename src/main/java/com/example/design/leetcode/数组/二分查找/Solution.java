package com.example.design.leetcode.数组.二分查找;

import java.util.Arrays;

/**
 * 二分查找
 */
public class Solution {
    public static  int search(int[] nums, int target) {
        if(nums==null||nums.length==0){
            return -1;
        }
        //先排好序
        Arrays.sort(nums);
        if(target>nums[nums.length-1]){
            return -1;
        }
        if(target<nums[0]){
            return -1;
        }
        int left = 0;
        int right = nums.length;
        while(left <= right){
            //pivot = (right+left)/2;
            int pivot = (right - left) / 2 + left;//同上恒等式，避免大数相加溢出
            if(target == nums[pivot]){
                return pivot;
            }
            else if(target>nums[pivot]){
                left = pivot+1;
            }else{
                right = pivot-1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = {-1,0,3,5,9,12};
        System.out.println(search(arr,9));
    }
}
