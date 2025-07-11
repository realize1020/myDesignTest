package com.example.design.leetcode.字符串.字符串匹配.kmp;


import java.util.Arrays;

public class Solution {

    /**暴力匹配
     * 复杂度分析时间复杂度：在最坏情况下，主串的每个字符都要和模式串的每个字符进行比较，时间复杂度为O(m * n)，这里的m是主串的长度，n是模式串的长度。
     * 空间复杂度：算法仅使用了常数级别的额外空间，空间复杂度为(O(1)。
     * @param a
     * @param b
     * @return
     */
    public static int force(String a,String b){
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        int i=0;
        int j=0;
        for(int k=0;k<x.length;k++){
            i=k;
            j=0;
            while(i<x.length && j<y.length){
                if(x[i]==y[j]){
                    i++;
                    j++;
                }else {
                    break;
                }
            }
            if(j==y.length){
                return i-j;
            }
        }

        return -1;
    }

    /**
     * kmp
     * @param a
     * @param b
     * @return
     */
    public static int getIndex(String a,String b){
        if(a.length()<b.length()){
            return -1;
        }
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        int[] next = getNextArray(y);
        int i=0;
        int j=0;
        while(i<x.length && j<y.length) {
            if (x[i] == y[j]) {
                i++;
                j++;
            } else if (j != 0) {
                j = next[j];
            } else{
                i++;
            }
        }

        return j==y.length ? i-j : -1;
    }

    private static int[] getNextArray(char[] y) {
        int[]next = new int[y.length];
        next[0]=-1;
        next[1]=0;
        int k=0;
        int i=2;
        while(i<y.length){
            if(y[i-1]==y[k]){
                next[i++] = ++k;
            }else if(k==0){
                i++;
            }else{
                k=next[k];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String s = "ababcabd";
        String pattern = "abd";
        System.out.println(Arrays.toString(getNextArray(s.toCharArray())));
        System.out.println(getIndex(s,pattern));
        System.out.println(force(s,pattern));
    }

}
