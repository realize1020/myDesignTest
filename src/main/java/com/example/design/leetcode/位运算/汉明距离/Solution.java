package com.example.design.leetcode.位运算.汉明距离;

/**
 * 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
 *
 * 给你两个整数 x 和 y，计算并返回它们之间的汉明距离。
 * 示例 1：
 *
 * 输入：x = 1, y = 4
 * 输出：2
 * 解释：
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 *        ↑   ↑
 * 上面的箭头指出了对应二进制位不同的位置。
 * 示例 2：
 *
 * 输入：x = 3, y = 1
 * 输出：1
 */
public class Solution {
    /**
     * 思路：转化为二进制字符数组，然后位数不够的，前面用0补够，然后逐一比较
     * @param x
     * @param y
     * @return
     */
    public static int hammingDistance(int x, int y) {
        String xStr = Integer.toBinaryString(x);
        String yStr = Integer.toBinaryString(y);
        int num = 0;
        String lessBit = "0"; //位数补够再比较
        StringBuilder sb = new StringBuilder();
        if(xStr.length()>yStr.length()) {
            int index = xStr.length() - yStr.length();
            while (index > 0) {
                sb.append(lessBit);
                index--;
            }
            yStr = sb.append(yStr).toString();
        }else{
            int index = yStr.length()-xStr.length();
            while(index>0){
                sb.append(lessBit);
                index--;
            }
            xStr = sb.append(xStr).toString();

        }
        for(int i=0;i<yStr.length();i++){
            if(xStr.charAt(i)!=yStr.charAt(i)){
                num++;
            }
        }
        return num;

    }

    /**
     * 使用异或运算，相同为0，不同为1。然后，统计1的个数，可以用bitCount方法
     * @param x
     * @param y
     * @return
     */
    public static int hammingDistance2(int x, int y){
        return Integer.bitCount(x ^ y);
    }

    /**
     *手动比较低位，然后移位计数。
     * @param x
     * @param y
     * @return
     */
    public static int hammingDistance3(int x, int y) {
        int s = x ^ y, ret = 0;
        while (s != 0) {
            ret += s ^ 1;
            s >>= 1;
        }
        return ret;
    }

    /**
     * s & s-1每循环一次都会把最右侧1给消除掉，直至为0为止
     * @param x
     * @param y
     * @return
     */
    public int hammingDistance4(int x, int y) {
        int s = x ^ y, ret = 0;
        while (s != 0) {
            s &= s - 1;
            ret++;
        }
        return ret;
    }

    public static void main(String[] args) {
       int num =  hammingDistance3(4,14);
        System.out.println(num);
    }
}
