package com.example.design.leetcode.递归迭代.汉诺塔问题;

public class Solution {

//    public static void f(Integer n){
//        leftToRight(n);
//    }
//
    public static void main(String[] args) {
        int n=3;
        //f(3);
        hanoi1(3);
    }
//
//    private static void leftToRight(Integer n) {
//        if(n==1){
//            System.out.println("左->右:"+n);
//            return;
//        }
//        System.out.println("左->中:"+n);
//        leftToMid(n-1);
//
//
//        midToRight(n-1);
//
//    }
//
//    private static void midToRight(int n) {
//        if(n==1){
//            System.out.println("中->右:"+n);
//            return;
//        }
//        System.out.println("中->左:"+n);
//        midToLeft(n-1);
//
//        leftToRight(n-1);
//    }
//
//    private static void midToLeft(int n) {
//        if(n==1){
//            System.out.println("中->左:"+n);
//            return;
//        }
//        System.out.println("中->右："+n);
//        midToRight(n-1);
//
//
//        rightToLeft(n-1);
//    }
//
//    private static void rightToLeft(int n) {
//        if(n==1){
//            System.out.println("右->左:"+n);
//            return;
//        }
//        System.out.println("右->中："+n);
//        rightToMid(n-1);
//
//
//        midToLeft(n-1);
//    }
//
//    private static void rightToMid(int n) {
//        if(n==1){
//            System.out.println("右->中:"+n);
//            return;
//        }
//
//        rightToLeft(n-1);
//
//        System.out.println("左->中:"+n);
//        leftToRight(n-1);
//    }
//
//    private static void leftToMid(int n) {
//        if(n==1){
//            System.out.println("左->中:"+n);
//            return;
//        }
//
//        leftToRight(n-1);
//
//        System.out.println("右->中:"+n);
//        rightToMid(n-1);
//    }

    public static void hanoi1(int n) {
        leftToRight(n);
    }

    // 请把1~N层圆盘 从左 -> 右
    public static void leftToRight(int n) {
        if (n == 1) {
            System.out.println("Move 1 from left to right");
            return;
        }
        leftToMid(n - 1);
        System.out.println("Move " + n + " from left to right");
        midToRight(n - 1);
    }

    // 请把1~N层圆盘 从左 -> 中
    public static void leftToMid(int n) {
        if (n == 1) {
            System.out.println("Move 1 from left to mid");
            return;
        }
        leftToRight(n - 1);
        System.out.println("Move " + n + " from left to mid");
        rightToMid(n - 1);
    }

    public static void rightToMid(int n) {
        if (n == 1) {
            System.out.println("Move 1 from right to mid");
            return;
        }
        rightToLeft(n - 1);
        System.out.println("Move " + n + " from right to mid");
        leftToMid(n - 1);
    }

    public static void midToRight(int n) {
        if (n == 1) {
            System.out.println("Move 1 from mid to right");
            return;
        }
        midToLeft(n - 1);
        System.out.println("Move " + n + " from mid to right");
        leftToRight(n - 1);
    }

    public static void midToLeft(int n) {
        if (n == 1) {
            System.out.println("Move 1 from mid to left");
            return;
        }
        midToRight(n - 1);
        System.out.println("Move " + n + " from mid to left");
        rightToLeft(n - 1);
    }

    public static void rightToLeft(int n) {
        if (n == 1) {
            System.out.println("Move 1 from right to left");
            return;
        }
        rightToMid(n - 1);
        System.out.println("Move " + n + " from right to left");
        midToLeft(n - 1);
    }

}
