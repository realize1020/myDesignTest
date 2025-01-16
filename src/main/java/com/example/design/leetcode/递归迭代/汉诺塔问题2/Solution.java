package com.example.design.leetcode.递归迭代.汉诺塔问题2;

public class Solution {
    public static void main(String[] args) {
        hanoi2(3);
    }
    public static void hanoi2(int n) {
        if (n > 0) {
            func(n, "left", "right", "mid");
        }
    }

    // 1~i 圆盘 目标是from -> to， other是另外一个
    public static void func(int N, String from, String to, String other) {
        if (N == 1) { // base
            System.out.println("Move 1 from " + from + " to " + to);
        } else {
            func(N - 1, from, other, to);
            System.out.println("Move " + N + " from " + from + " to " + to);
            func(N - 1, other, to, from);
        }
    }
}
