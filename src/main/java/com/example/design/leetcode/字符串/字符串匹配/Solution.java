package com.example.design.leetcode.字符串.字符串匹配;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    public static void main(String[] args) {
//        String str= "abcdefg";
//        String matchStr = "deg";
//        boolean match = match(str, matchStr);
//        System.out.println(match);

//        System.out.println(match("hello", "lo")); // true
//        System.out.println(match("hello", "world")); // false
//        System.out.println(match("abcabcabc", "abc")); // true
//        System.out.println(match("abcabcabc", "abcd")); // false
//        System.out.println(match("abcabcabc", "")); // true
//        System.out.println(match("", "a")); // false

        System.out.println(BM_Match2("abcdefg", "cde"));
//
//        System.out.println(horspoolSearch2("abcabcabcd", "bc"));

        //System.out.println(match2("hello", "lo"));

        //int[] abcdebcds = createShiftTable("abcdebcd");
    }


    /**
     * 暴力匹配算法手写，为什么 while(i<str.length()&&j<matchStr.length())里面又加了一个判断i<str.length()，因为else里面有i++;，改变了i所以需要判断，不然str.charAt(i)会出现数组越界
     * 还有就是if(isMatch&&j==matchStr.length())为什么增加了j==matchStr.length()，因为如果str最后一个字符和matchStr的从零开始到倒数第二个字符都相同的话，
     * matchStr最后一个字符没有办法被匹配，所以需要根据它的长度判断它有没有被匹配完
     * @param str
     * @param matchStr
     * @return
     */
    public static boolean match(String str,String matchStr){
        int i=0;
        int j=0;
        int temp =0;
        boolean isMatch =false;
        while(i<str.length()){//i<str.length-matchStr.length()
            temp=i+1;
            while(i<str.length()&&j<matchStr.length()){
                if(str.charAt(i)!=matchStr.charAt(j)){
                    i++;
                    isMatch=false;
                    break;
                }else{
                    i++;
                    j++;
                    isMatch=true;
                }
            }
            if(isMatch&&j==matchStr.length()){
                return true;
            }else{
                j=0;
                i=temp;
            }
        }
        return isMatch;
    }

    /**
     * 暴力解法最简单形式
     * @param str
     * @param matchStr
     * @return
     */
    public static int match2(String str,String matchStr){
        int i=0;
        int j=0;
        while(i<str.length()&&j<matchStr.length()){//i<str.length-matchStr.length()
            if(str.charAt(i)==matchStr.charAt(j)){
                i++;
                j++;
            }else {
                i = i - j + 1; // 回退文本串的索引，回退到上一次匹配的下一个位置
                j = 0;// 重置模式串的索引
            }
        }
        if(j==matchStr.length()){
            return i-j;// 返回匹配的起始位置
        }
        return -1;// 未找到匹配
    }


    /**
     * 暴力匹配算法，AI改进后
     * @param str
     * @param matchStr
     * @return
     */
    public static boolean match3(String str, String matchStr) {
        int i = 0;
        int j = 0;

        while (i <= str.length() - matchStr.length()) { // 确保不会越界
            j = 0; // 每次外层循环开始时重置 j
            while (j < matchStr.length()) {
                if (str.charAt(i + j) != matchStr.charAt(j)) {
                    break; // 字符不匹配，跳出内层循环
                }
                j++;
            }

            if (j == matchStr.length()) { // 如果 j 达到了 matchStr 的长度，说明匹配成功
                return true;
            }
            i++; // 外层循环继续
        }

        return false; // 如果没有找到匹配，返回 false
    }

    /**
     * AI生成暴力匹配算法
     * @param text
     * @param pattern
     * @return
     */
    public static int bruteForce(String text, String pattern) {

        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j;
            for (j = 0; j < pattern.length(); j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == pattern.length()) {
                return i; // 匹配成功
            }
        }
        return -1; // 未找到匹配
    }

    /**
     * ai的Boyer-Moore算法
     * @param text
     * @param pattern
     * @return
     */
    public static int horspoolSearch(String text, String pattern) {

        if (text.length() < pattern.length()) {
            return -1; // 文本串比模式串短，不可能匹配
        }

        // 创建跳跃表
        int[] shiftTable = createShiftTable(pattern);

        int i = pattern.length() - 1; // 模式串的末尾字符索引
        while (i <= text.length() - 1) {
            int k = 0;
            while (k < pattern.length() && pattern.charAt(pattern.length() - 1 - k) == text.charAt(i - k)) {
                k++;
            }
            if (k == pattern.length()) {
                return i - pattern.length() + 1; // 找到匹配
            } else {
                // 根据跳跃表确定下一个比较的位置
                i += shiftTable[text.charAt(i)];
            }
        }
        return -1; // 未找到匹配
    }



    /**
     * 给模式串构建跳跃表
     * @param pattern
     * @return
     */
    private static int[] createShiftTable(String pattern) {
        int n = pattern.length();
        int[] shiftTable = new int[256]; // 假设 ASCII 字符集
        Arrays.fill(shiftTable, n); // 默认值为模式串的长度

        // 遍历模式串（除了最后一个字符），记录每个字符在模式串中的最后出现位置
        for (int i = 0; i < n - 1; i++) {
            shiftTable[(int) pattern.charAt(i)] = n - 1 - i; // 记录字符在模式串中的最后出现位置.下标
        }

        return shiftTable;
    }





    private static int BM_Match(String s,String pattern){
        int i=pattern.length()-1;
        int index=pattern.length()-1;
        while(i<s.length()){//我这个写法，i不能是i<=s.length()-pattern.length()，因为这个i是从后往前的回退。不是别人的那种charAt(s.length-i)
            while(index>=0&&pattern.charAt(index)==s.charAt(i)){
                i--;
                index--;
            }
            if(index==-1){
                return i+1;
            }else{
                i = i + moveDistance(s.charAt(i), pattern);
                index=pattern.length()-1;
            }

        }
        return -1;
    }

    /**
     * 这个可以优化一下，因为每次都需要char[] charArray = pattern.toCharArray();然后for循环寻找，效率低
     * 可以提前构造好这个字符移动距离数组
     * @param c
     * @param pattern
     * @return
     */
    private static int moveDistance(char c, String pattern) {
        char[] charArray = pattern.toCharArray();
        for(int i=charArray.length-1;i>=0;i--){
            if(charArray[i]==c){
                return charArray.length-1-i;
            }
        }
        return charArray.length;
    }

    private static int BM_Match2(String s,String pattern){
        int i=pattern.length()-1;
        int index=pattern.length()-1;
        List<Integer> moveDist = moveDistance2(pattern);
        while(i<s.length()){//我这个写法，i不能是i<=s.length()-pattern.length()，因为这个i是从后往前的回退。不是别人的那种charAt(s.length-i)
            while(index>=0&&pattern.charAt(index)==s.charAt(i)){
                i--;
                index--;
            }
            if(index==-1){
                return i+1;
            }else{
                i = i + (moveDist.contains(Integer.valueOf(s.charAt(i)))?moveDist.indexOf(Integer.valueOf(s.charAt(i))):pattern.length());
                index=pattern.length()-1;
            }

        }
        return -1;
    }

    private static List<Integer> moveDistance2(String pattern){
        int[] moveDistance = new int[pattern.length()];
        Arrays.fill(moveDistance,pattern.length());
        for(int i=0;i<moveDistance.length;i++){
            moveDistance[moveDistance.length-1-i]=pattern.charAt(i);
        }
        
        return Arrays.stream(moveDistance).boxed().collect(Collectors.toList());
    }


    private static HashMap<Integer,Integer> createShiftTable2(String pattern){
        HashMap hashMap =new HashMap<Integer,Integer>(256);

        for(int i=0;i<256;i++){
            hashMap.put(i,pattern.length());
        }

        for(int i=0;i<pattern.length()-1;i++){
            hashMap.put((int)pattern.charAt(i),pattern.length()-1-i);
        }

        return hashMap;
    }

    /**
     * 自己手写
     * @param str
     * @param pattern
     * @return
     */
    public static int horspoolSearch2(String str,String pattern){
        HashMap<Integer, Integer> shiftTable = createShiftTable2(pattern);
        int i= pattern.length()-1;
        int index=0;
        while(i>0&&index+i<str.length()){
            while(i>=0&&index+i<str.length()&&str.charAt(i+index)==pattern.charAt(i)){
                i--;
            }
            if(i==-1){
                return i+index+1;
            }else{
                index+=shiftTable.get((int)str.charAt(i+index));
            }

        }

        return -1;
    }

}
