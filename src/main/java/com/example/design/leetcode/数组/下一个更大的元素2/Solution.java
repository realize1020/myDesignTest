package com.example.design.leetcode.数组.下一个更大的元素2;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 503. 下一个更大元素 II

 给定一个循环数组 nums （ nums[nums.length - 1] 的下一个元素是 nums[0] ），返回 nums 中每个元素的 下一个更大元素 。

 数字 x 的 下一个更大的元素 是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。
 如果不存在，则输出 -1 。
 示例 1:
 输入: nums = [1,2,1]
 输出: [2,-1,2]
 解释: 第一个 1 的下一个更大的数是 2；
 数字 2 找不到下一个更大的数；
 第二个 1 的下一个最大的数需要循环搜索，结果也是 2。

 示例 2:
 输入: nums = [1,2,3,4,3]
 输出: [2,3,4,-1,4]
 提示：用栈解决效率更好
 */
public class Solution {

    public static void main(String[] args) {
        int [] nums = {1,2,1};
        int[] ints = nextGreaterElements2(nums);
        System.out.println(Arrays.toString(ints));
    }
    public static int[] nextGreaterElements(int[] nums) {

        int[] plus = new int[nums.length];

        for(int i=0;i<nums.length;i++){
            int index = i;
            int length = nums.length;
            boolean circle = false;
            for(int j=index+1;j<=length;j++){
                if(!circle&&j==nums.length){
                    length=index;//设置第二次退出的位置
                    j=0;//重置j
                    circle=true;//标记已经循环过一次了
                }

                if(nums[index]<nums[j]){
                    plus[i]=nums[j];
                    break;
                }
                if(circle&&j==length){
                    plus[i]=-1;
                }
            }

        }
        return plus;
    }

    /**
     * leetcode官方：使用栈解决，宝贝们，一句话总结：凡是看到寻找下一个最大/小的这种题目，必选单调栈，然后会有许多变种，可以具体情形具体处理
     * @param nums
     * @return
     */

    public static int[] nextGreaterElements2(int[] nums) {
        int n = nums.length;
        int[] ret = new int[n];
        Arrays.fill(ret, -1);
        Deque<Integer> stack = new LinkedList<Integer>();//存的数组下标，且这个栈是单调递减的
        for (int i = 0; i < n * 2 - 1; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n]) {
                ret[stack.pop()] = nums[i % n];
            }
            stack.push(i % n);
        }
        return ret;
    }
}
