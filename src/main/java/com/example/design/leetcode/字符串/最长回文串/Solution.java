package com.example.design.leetcode.字符串.最长回文串;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 1. 复杂度：O(N)
 * 2. 概念：
 * 回文直径：以 i 为中心，两边能够扩展到的
 * 回文半径：以 i 为中心，一边能够扩展到的
 *
 * 1）回文半径数组（p）
 * 记录每个位置的回文半径
 *
 * 2）最右回文右边界（mx）
 * 表示到目前为止在向右扩展时，能到达的最右边的位置。初始为-1
 *
 * 3）回文右边界的中心（id）
 * 表示第一次到达此右边界的位置
 *
 * 3. 马拉车算法的扩展情况
 * 有两种可能性，第2种可能性分了3种情况
 *
 * 假设从 i 位置开始扩展
 *
 * 可能性1：i不在回文右边界mx内
 * 1）i不在回文右边界里面
 * 复杂度：O（N）
 * 此时直接暴力扩展
 *
 * 可能性2：i在回文右边界 mx 内，包括以下三种情况
 * 2）i在回文右边界里面，i’的回文半径在回文左边界 L 里面
 * 复杂度：O(1)
 * 此时i位置的回文区域不用扩展，i的回文半径与 i’ 一样
 *
 * 3）i在回文右边界里面，i’的回文半径在回文左边界 L 外
 * 复杂度：O(1)
 * 此时i位置的回文区域不用扩展，i的回文半径就是 mx - i
 *
 * 4）i在回文右边界里面，i’的回文半径与回文左边界 L 压线
 * 复杂度：O(N)
 * 此时需要试 mx 右边的区域是否能够构成回文
 */
public class Solution {
    /**
     * 当 i=4（字符 c），初始 pArr[4]=1，扩展后半径为 5（覆盖整个字符串），此时 R=9, C=4。
     * 最终 max=5，返回 5-1=4，对应原始回文 "abcba" 长度 5？ 等等，这里需要注意：实际计算中，预处理后的回文半径是从中心到边界的距离，
     * 例如中心 c 的半径是 5（包含 #a#b#c#b#a#），对应原始回文长度为 5-1=4？ 不，实际原始回文长度应为半径 - 1，因为每个分隔符占一个位置。
     * 正确计算：原始回文长度 = 半径 ×2-1 - 分隔符数量。Manacher 算法中，直接返回 max-1 是因为预处理后的字符串中，每个原始字符对应一个分隔符，所以最长回文子串长度为 (max-1)。
     * @param s
     * @return
     */
    public static int manacher(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        // "12132" -> "#1#2#1#3#2#"
        char[] str = manacherString(s); // 预处理后的字符串
        int[] pArr = new int[str.length]; // pArr[i] 表示以 str[i] 为中心的回文半径（包含自身）
        int C = -1; // 最右回文中心（初始无效）
        int R = -1; // 最右回文边界的下一个位置（初始无效）
        int max = Integer.MIN_VALUE; // 记录最大回文半径

            for (int i = 0; i < str.length; i++) {
            // 步骤1：利用对称性，确定pArr[i]的初始值
            pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1; // 关键对称性推导

            // 步骤2：扩展回文半径
            while (i + pArr[i] < str.length && i - pArr[i] > -1) {
                if (str[i + pArr[i]] == str     [i - pArr[i]]) {
                    pArr[i]++; // 半径扩大
                } else {
                    break; // 字符不匹配，停止扩展
                }
            }

            // 步骤3：更新最右回文边界和中心
            if (i + pArr[i] > R) {
                R = i + pArr[i]; // 新的最右边界
                C = i; // 新的回文中心
            }

            // 步骤4：更新最大回文半径
            max = Math.max(max, pArr[i]);
        }

        // 原始字符串的最长回文长度 = 最大半径 - 1（分隔符占了长度）
        return max - 1;
    }


    public static char[] manacherString(String str) {
        char[] charArr = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];
        int index = 0;
        for (int i = 0; i != res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : charArr[index++];//偶数位置赋值#,奇数位置原来的数据
        }
        return res;
    }


    /**
     * 我的manacher算法实现
     * @param str
     * @return
     */
    public static int getLength(String str){
        char[] r = handle(str);

        int a[]=new int[r.length];
        int center =-1;
        int R = -1;
        int max=Integer.MIN_VALUE;
        for(int i=0;i<r.length;i++){
            if(i<=R){
                if((2*center-i)<(2*center-R)){//i'在左边R的外面
                    a[i]=R-i;
                }else if((2*center-i)>(2*center-R)){//i'在左边R的里面
                    a[i]=a[2*center-i];
                }else{//压线，看右R的右边和左R的左边能不能构成回文
                    if(r[R+1]==r[2*center-R-1]){
                        R++;
                        a[i]=R-center;
                    }
                }
            }else{
                int k=1;
                while(i-k>-1&&i+k<r.length){
                    if(r[i-k]==r[i+k]){
                        a[i]=k-i+1;
                    }
                }
                center=k;
                R=k;
            }

            max=Math.max(max,a[i]);

        }

        return max;
    }

    private static char[] handle(String str) {
        char[] chars = str.toCharArray();
        int j=0;
        char[] c =new char[chars.length*2+1];
        for(int i=0;i<c.length;i++){
            if((i&1)==0){
                c[i]='#';
            }else{
                c[i]=chars[j++];
            }

        }
        return c;
    }

    public static void main(String[] args) {
        String s = "12132";
        int manacher = manacher(s);
        System.out.println(manacher);

        System.out.println(getLength(s));
    }
}
