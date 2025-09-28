package com.example.design.leetcode.递归迭代.连续正数的和;

import com.baomidou.mybatisplus.extension.api.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 定义一种数：可以表示成若干（数量>1）连续正数和的数 比如: 5 = 2+3，5就是这样的数 12 = 3+4+5，12就是这样的数 1不是这样的数，
 * 因为要求数量大于1个、连续正数和 2 = 1 + 1，2也不是，因为等号右边不是连续正数 给定一个参数N，返回是不是可以表示成若干连续正数和的数
 */
public class Solution {
    /**
     * 时间复杂度O(n^2)
     * @param n
     * @return
     */
    public static int findNum(int n){
        int num = n-1;
        int sum = num;
        List<Integer> list = new ArrayList<>();
        for(int i=1;i<n;i++){
            list.clear();
            num = n-i;
            sum=num;
            list.add(num);
            while(sum < n && num>=0){
                num--;
                sum+=num;
                list.add(num);
            }
            if(sum == n){
                String str = list.stream().map(String::valueOf).collect(Collectors.joining("+"));
                System.out.println(n+"="+str);
                return n;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        for(int i=1;i<=100;i++){
            System.out.println(findNum(i));//发现2的次幂都不是
        }

    }

    public static boolean is2(int num) {
//      return num == (num & (~num + 1)); //~num + 1等价于-num,负数等于按位取反加1
//
//		return num == (num & (-num)); //只有2的幂次方数满足 num == (num & (-num)) 这一条件
        return (num & (num - 1)) != 0;
    }
}
