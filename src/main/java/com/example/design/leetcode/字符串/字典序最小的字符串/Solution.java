package com.example.design.leetcode.字符串.字典序最小的字符串;

import java.util.ArrayList;

public class Solution {
    public static void main(String[] args) {
    }

//    public String getMinString(String str) {
//        char[] chars = str.toCharArray();
//        int n = chars.length;
//        int[] index = new int[n];
//        for (int i = 0; i < n; i++) {
//            index[i] = i;
//        }
//        for (int i = 0; i < n - 1; i++) {
//            int min = i;
//            for (int j = i + 1; j < n; j++) {
//                if (chars[index[j]] < chars[index[min]]){
//                    min = j;
//                }
//            }
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < n; i++) {
//            sb.append(chars[index[i]]);
//        }
//        return sb.toString();
//    }

    public static String getMinString(String[] strs){
        if(strs==null||strs.length==0){
            return null;
        }
        ArrayList<Integer> use =new ArrayList<>();
        String path="";
        String[] all=proceed(strs,use,path);
        return null;
    }

    private static String[] proceed(String[] strs, ArrayList<Integer> use, String path) {
        for(int i=0;i<strs.length;i++){
            String s = strs[i];

        }
        return null;
    }
}
