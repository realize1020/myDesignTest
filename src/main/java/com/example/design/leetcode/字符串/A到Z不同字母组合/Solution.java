package com.example.design.leetcode.字符串.A到Z不同字母组合;

public class Solution {
    public static int number(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return process(str.toCharArray(), 0);
    }

    public static int process(char[] str, int i) {
        if (i == str.length) { // base case: 已经处理完所有字符
            return 1;
        }
        if (str[i] == '0') { // '0' 不能单独解码，也不能与前一个字符组成有效编码
            return 0;
        }
        if (str[i] == '1') { // '1' 可以单独解码，也可以与下一个字符组成 '10' 到 '19'
            int res = process(str, i + 1); // 单独解码
            if (i + 1 < str.length) {
                res += process(str, i + 2); // 与下一个字符一起解码
            }
            return res;
        }
        if (str[i] == '2') { // '2' 可以单独解码，也可以与下一个字符组成 '20' 到 '26'
            int res = process(str, i + 1); // 单独解码
            if (i + 1 < str.length && (str[i + 1] >= '0' && str[i + 1] <= '6')) {
                res += process(str, i + 2); // 与下一个字符一起解码
            }
            return res;
        }
        return process(str, i + 1); // 其他字符只能单独解码
    }


    public static void main(String[] args) {
        int number = number("10");
        System.out.println(number);
    }

}
