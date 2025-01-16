package com.example.design.leetcode.贪心算法.最少需要放置多少盏灯;

import java.util.HashSet;

public class Solution {
    public static int minLight1(String road) {
        if (road == null || road.length() == 0) {
            return 0;
        }
        return process(road.toCharArray(), 0, new HashSet<>());
    }

    // str[index....]位置，自由选择放灯还是不放灯
    // str[0..index-1]位置呢？已经做完决定了，那些放了灯的位置，存在lights里
    // 要求选出能照亮所有.的方案，并且在这些有效的方案中，返回最少需要几个灯
    public static int process(char[] str, int index, HashSet<Integer> lights) {
        if (index == str.length) { // 如果已经遍历完所有位置，检查是否所有空位都被照亮,// 结束的时候
            for (int i = 0; i < str.length; i++) {
                if (str[i] != 'X') { // 当前位置是点的话
                    if (!lights.contains(i - 1)
                            && !lights.contains(i)
                            && !lights.contains(i + 1)) {
                        return Integer.MAX_VALUE; // 如果当前位置是空位（'.'），并且它和它的左右两边都没有灯，则返回无穷大表示无效方案
                    }
                }
            }
            // 如果所有空位都被照亮，返回放置的灯的数量
            return lights.size();
        } else { // str还没结束
            // 不放置灯的情况
            int no = process(str, index + 1, lights);
            // 放置灯的情况（仅当当前位置是空位时才考虑）
            int yes = Integer.MAX_VALUE;
            if (str[index] == '.') {
                // 在当前位置放置一盏灯
                lights.add(index);
                // 继续处理下一个位置
                yes = process(str, index + 1, lights);
                // 回溯：移除当前位置的灯，以便尝试其他可能性
                lights.remove(index);
            }
            return Math.min(no, yes);
        }
    }

    /**
     * 这个算法比较好理解
     * @param road
     * @return
     */
    public static int minLight2(String road) {
        char[] str = road.toCharArray();
        int index = 0;
        int light = 0;
        while (index < str.length) {
            if (str[index] == 'X') {
                index++;
            } else { // i -> .
                light++;
                if (index + 1 == str.length) {
                    break;
                } else {
                    if (str[index + 1] == 'X') {
                        index = index + 2;
                    } else {
                        index = index + 3;
                    }
                }
            }
        }
        return light;
    }

    public static void main(String[] args) {
        String road="XXXX";
        int i = minLight2(road);
        System.out.println("需要灯数："+i);
    }

}
