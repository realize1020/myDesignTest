package com.example.design.leetcode.从暴力递归到动态规划;

public class CoinsWay {
    public static int ways1(int[] arr, int aim) {
        // 如果输入数组为空、长度为0或者目标金额小于0，则返回0
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        // 调用辅助方法 process1，从第0个面值开始考虑，剩余需要凑的目标金额为 aim
        return process1(arr, 0, aim);
    }

    /**
     * 总感觉和字符串全排列算法或者和字典序算法相似
     * @param arr
     * @param index
     * @param rest
     * @return
     */
    public static int process1(int[] arr, int index, int rest) {
        // 如果已经考虑完所有面值，检查是否正好凑够了目标金额
        if(index == arr.length) {
            return rest == 0 ? 1 : 0;
        }

        int ways = 0;

        // 尝试使用当前面值的不同数量（从0张到最多可以使用的张数）
        for(int zhang = 0;  zhang * arr[index] <= rest ;zhang++) {
            // 递归调用 process1，尝试使用 zhang 张当前面值的硬币
            ways += process1(arr, index + 1, rest - (zhang * arr[index]));
        }

        return ways;
    }

    public static void main(String[] args) {
        int arr[] ={1,1,1};
        int aim = 2;
        System.out.println(ways1(arr, aim));
    }
}
