package com.example.design.leetcode.字符串.全排列;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 全排列的算法实现，对strs数组中的的字符串实现全排列
 * 全排列：将原字符串中的所有字符重新排序形成的所有可能的组合。
 * 对于字符串 "abc"，它的全排列包括 "abc", "acb", "bac", "bca", "cab", 和 "cba"。
 *
 */
public class Solution {
    public static ArrayList<String> permutation(String str) {
        ArrayList<String> res = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return res;
        }
        char[] chs = str.toCharArray();
        process(chs, 0, res);
        return res;
    }

    // str[0..i-1]已经做好决定的
    // str[i...]都有机会来到i位置
    // i终止位置，str当i前的样子，就是一种结果 -> ans
    public static void process(char[] str, int i, ArrayList<String> ans) {
        if (i == str.length) {
            ans.add(String.valueOf(str));
        }
        // 如果i没有终止，i...  都可以来到i位置
        for (int j =i; j < str.length; j++) { // j  i后面所有的字符都有机会
            swap(str, i, j);
            process(str, i + 1, ans);
            swap(str, i, j);
        }
    }


    public static ArrayList<String> permutationNoRepeat(String str) {
        ArrayList<String> res = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return res;
        }
        char[] chs = str.toCharArray();
        process2(chs, 0, res);
        return res;
    }

    // str[0..i-1]已经做好决定的
    // str[i...]都有机会来到i位置
    // i终止位置，str当前的样子，就是一种结果 -> ans
    public static void process2(char[] str, int i, ArrayList<String> res) {
        if (i == str.length) {
            res.add(String.valueOf(str));
            return;
        }
        boolean[] visit = new boolean[26]; // visit[0 1 .. 25]
        for (int j = i; j < str.length; j++) {
            // str[j] = 'a'   -> 0   visit[0] -> 'a'

            // str[j] = 'z'   -> 25   visit[25] -> 'z'
            if (!visit[str[j] - 'a']) {

                visit[str[j] - 'a'] = true;
                swap(str, i, j);
                process2(str, i + 1, res);
                swap(str, i, j);

            }
        }
    }

    public static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }

    public static ArrayList<String> permutation2(String str){
        ArrayList<String> all =new ArrayList<>();
        HashSet<String> use =new HashSet();
        char[] chars = str.toCharArray();
        process(chars,use,"",all);
        return all;
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
    private static void process(char[] strs, HashSet<String> use, String path, ArrayList<String> all) {

        if(path.length()==strs.length){
            all.add(path);
        }else{
            for(int i=path.length();i<strs.length;i++){
//                if(!use.contains(strs[i]+"")){
//                    use.add(strs[i]+"");
//                    process(strs,use,path+strs[i],all);
//                    use.remove(strs[i]+"");
//                }
                use.add(strs[i]+"");
                process(strs,use,path+strs[i],all);
                use.remove(strs[i]+"");
            }
        }

    }


    public static void main(String[] args) {
        String s = "aac";
        List<String> ans1 = permutation(s);
        for (String str : ans1) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans2 = permutationNoRepeat(s);
        for (String str : ans2) {
            System.out.println(str);
        }
        System.out.println("=======");
        ArrayList<String> ans3 = permutation2(s);
        ans3.forEach(a-> System.out.println(a));
    }
}
