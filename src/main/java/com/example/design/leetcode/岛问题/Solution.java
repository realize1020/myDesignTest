package com.example.design.leetcode.岛问题;

/**
 * 一个矩阵中只有0和1两种值，每个位置都可以和自己的上、下、左、右四个位置相连，如果有一片1连在一起，这个部分叫做一个岛，求一个矩阵中有多少个岛？
 * 举例：
 * 0 0 1 0 1 0
 * 1 1 1 0 1 0
 * 1 0 0 1 0 0
 * 0 0 0 0 0 0
 * 这个矩阵中有三个岛。
 *
 * 2.分析
 * 本题的思路很简单，对矩阵中的每个元素进行遍历。如果当前元素为1时，岛的数量加1，再把当前元素改为2（表示这个岛已经记录过），递归查找其上下左右四个元素，如果有元素为1，继续改为2（相邻为1是同一个岛，已经记录过），不断递归上下左右四个元素直至元素不为1或查找越界。
 */
public class Solution {

    private static int width = 6;
    private static int height = 4;

    void infect(int a[][],int j,int i)
    {
        if(j < 0||j >= width ||i < 0|| i >= height || a[i][j] != 1)
            return;
        a[i][j] = 2;
        infect(a,j - 1,i);
        infect(a,j + 1,i);
        infect(a,j,i - 1);
        infect(a,j,i + 1);
    }

    int countIslands(int a[][])
    {
        int num = 0;
        for(int i = 0;i < height;i++)
        {
            for(int j = 0;j < width;j++)
            {
                if(a[i][j] == 1)
                {
                    num++;
                    infect(a,j,i);
                }
            }
        }
        return num;
    }


    public static void main(String[] args) {
        int a[][] = new int[][]{
                {0,0,1,0,1,0},
                {1,1,1,0,1,0},
                {1,0,0,1,0,0},
                {0,0,0,0,0,0}
        };
        System.out.println(new Solution().countIslands(a));
    }
}
