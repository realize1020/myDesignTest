package com.example.design.leetcode.数组.求正数数组的所有子数组中所有元素相加和小于等于k的最长子数组长度;

/**
 * 给定一个无序数组 arr，其中元素可正、可负、可0，给定一个整数 k。求 ar 所有的子数组中累加和小于或等于k的最长子数组长度。
 * 例如:arr=[3.-2.-4.0.6]，k=-2，相加和小于或等于-2 的最长子数组为{3.-2.-4.0}，所以结果返回 4。
 */
public class Solution {
    /**
     * 递推关系 ：
     * -如果 minSums[i+1] < 0 ：说明继续扩展能得到更小的和
     * -如果 minSums[i+1] ≥ 0 ：说明单独从i开始更好
     * @param arr
     * @param k
     * @return
     */
    public static int maxLengthAwesome(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] minSums = new int[arr.length];//表示从位置i开始向右扩展能得到的最小累加和
        int[] minSumEnds = new int[arr.length];//表示从位置i开始达到最小累加和的结束位置
        minSums[arr.length - 1] = arr[arr.length - 1];
        minSumEnds[arr.length - 1] = arr.length - 1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (minSums[i + 1] < 0) {
                minSums[i] = arr[i] + minSums[i + 1];
                minSumEnds[i] = minSumEnds[i + 1];//把右边给左边
            } else {
                minSums[i] = arr[i];
                minSumEnds[i] = i;
            }
            //改成这样也可以
/*            minSums[i]=Math.min(arr[i], arr[i]+minSums[i+1]);
            if(minSums[i+1]<0){
                minSumEnds[i] = minSumEnds[i + 1];
            }else{
                minSumEnds[i] = i;
            }*/
        }
        // 迟迟扩不进来那一块儿的开头位置
        int end = 0;
        int sum = 0;
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            // while循环结束之后：
            // 1) 如果以i开头的情况下，累加和<=k的最长子数组是arr[i..end-1]，看看这个子数组长度能不能更新res；
            // 2) 如果以i开头的情况下，累加和<=k的最长子数组比arr[i..end-1]短，更新还是不更新res都不会影响最终结果；
            while (end < arr.length && sum + minSums[end] <= k) {
                sum += minSums[end];
                end = minSumEnds[end] + 1;
            }
            ans = Math.max(ans, end - i);
            if (end > i) { // 还有窗口，哪怕窗口没有数字 [i~end) [4,4)
                sum -= arr[i];
            } else { // i == end,  即将 i++, i > end, 此时窗口概念维持不住了，所以end跟着i一起走
                end = i + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {1,-2,-3,4,5};
        System.out.println(maxLengthAwesome(arr, 7));
    }
}
