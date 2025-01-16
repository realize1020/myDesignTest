package com.example.design.leetcode.字符串.字典序最小的组合字符串;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Solution {

    public static String lowString(String[] strs){
        if(strs.length==0||strs==null){
            return null;
        }
        ArrayList<String> all =new ArrayList<>();
        HashSet<String> use = new HashSet<String>();
        String path="";
        process(strs,use,path,all);
        String lowest=all.get(0);
        for(int i=1;i<all.size();i++){
            if(all.get(i).compareTo(lowest)< 0){
                lowest=all.get(i);
            }
        }
        return lowest;
    }

    /**
     * 全排列的算法实现，对strs数组中的的字符串实现全排列，这个方法不能有重复字符串
     * 全排列：将原字符串中的所有字符重新排序形成的所有可能的组合。
     * 对于字符串 "abc"，它的全排列包括 "abc", "acb", "bac", "bca", "cab", 和 "cba"。
     * @param strs
     * @param use
     * @param path
     * @param all
     */
    private static void process(String[] strs, HashSet<String> use, String path,ArrayList<String> all) {

        if(use.size()==strs.length){
            all.add(path);
        }else{
            for(int i=0;i<strs.length;i++){
                if(!use.contains(strs[i])){
                    use.add(strs[i]);
                    process(strs,use,path+strs[i],all);
                    use.remove(strs[i]);
                }
            }
        }

    }


    private static void process2(String[] strs){
        ArrayList<String> all=new ArrayList<>();
        for(int i=0;i<strs.length;i++){
            for(int j=0;j<strs.length;j++){
                for(int k=0;k<strs.length;k++){
                    if(i!=j&&i!=k&&j!=k){
                        String str = strs[i]+strs[j]+strs[k];
                        all.add(str);
                    }
                }
            }
        }
        System.out.println(all);

    }


    public static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {//比较逻辑是将两个字符串 a 和 b 分别拼接成 ab 和 ba，然后比较这两个拼接结果的字典序。
            return (a + b).compareTo(b + a);//如果 ab 小于 ba，则认为 a 应该排在 b 前面；反之亦然。
        }
    }

    /**
     * 实现了将一个字符串数组按照特定规则排序，以生成字典序最小的组合字符串。
     * 这个逻辑的核心在于自定义比较器 MyComparator，它通过比较两个字符串拼接后的结果来决定排序顺序。
     * @param strs
     * @return
     */
    public static String lowestString2(String[] strs) {
        if (strs == null || strs.length == 0) {//首先检查输入数组是否为空或长度为 0，如果是，则返回空字符串。
            return "";
        }
        Arrays.sort(strs, new MyComparator());//使用 Arrays.sort 方法和自定义的 MyComparator 对字符串数组进行排序。
        String res = "";
        for (int i = 0; i < strs.length; i++) {//最后，将排序后的字符串数组拼接成一个字符串并返回。
            res += strs[i];
        }
        return res;
    }

    public static void main(String[] args) {
        //String[] strs={"ab","bc","bc"};//要保证字符串数组中的元素不能重复，如果重复的话，use中会有已经包含的字符串
        //然后use里就会少加一个，导致最后这个判断if(use.size()==strs.length)不成立，然后直接就走下面remove的方法，最后整个代码报错
        String[] strs={"ab","bc","cd"};

        System.out.println(lowString(strs));
        process2(strs);
    }
}
