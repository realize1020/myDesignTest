package com.example.design.leetcode.二叉树.树状数组;

import java.util.Arrays;

public class Solution {
    public static class IndexTree {

        private int[] tree;
        private int N;

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];//下标从1开始，所以多一个数
        }

        public int sum(int index) {
            int ret = 0;
            while (index > 0) {
                ret += tree[index];
                index -= index & -index;// 去掉一个自己最右侧的1
            }
            return ret;
        }

        public void add(int index, int d) {
            while (index <= N) {
                tree[index] += d;
                index += index & -index;// 加一个自己最右侧的1
            }
        }
    }

    public static void main(String[] args) {
        //一维IndexTree
        /*IndexTree  indexTree = new IndexTree(5);
        indexTree.add(1,1);
        indexTree.add(2,2);
        indexTree.add(3,3);
        indexTree.add(4,4);
        indexTree.add(5,5);
        System.out.println(Arrays.toString(indexTree.tree));
        System.out.println(indexTree.sum(4));*/
        IndexTree2D indexTree2D = new IndexTree2D(new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        });

        System.out.println(indexTree2D.sum(1, 1));
    }


    /**
     * 二维IndexTree（树状数组）
     * 支持高效的二维区间和查询与单点更新操作
     * - 单点更新：O(logN * logM)
     * - 区间查询：O(logN * logM)
     */
    public static class IndexTree2D {
        private int[][] tree;  // 树状数组，存储区间和信息
        private int[][] nums;  // 原始矩阵的副本，用于计算差值
        private int N;         // 矩阵行数
        private int M;         // 矩阵列数

        /**
         * 构造函数，初始化二维树状数组
         * @param matrix 原始二维矩阵
         */
        public IndexTree2D(int[][] matrix) {
            if (matrix.length == 0 || matrix[0].length == 0) {
                return;
            }
            N = matrix.length;
            M = matrix[0].length;
            tree = new int[N + 1][M + 1];  // 树状数组下标从1开始
            nums = new int[N][M];          // 存储原始值，用于计算更新差值

            // 初始化树状数组：遍历每个元素并调用update方法
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    update(i, j, matrix[i][j]);
                }
            }
        }

        /**
         * 计算从(0,0)到(row,col)的二维前缀和
         * @param row 行下标（原始矩阵下标，0-based）
         * @param col 列下标（原始矩阵下标，0-based）
         * @return 前缀和
         */
        private int sum(int row, int col) {
            int sum = 0;
            // 树状数组下标从1开始，因此转换为row+1和col+1
            for (int i = row + 1; i > 0; i -= i & (-i)) {
                for (int j = col + 1; j > 0; j -= j & (-j)) {
                    sum += tree[i][j];  // 累加所有覆盖当前点的区间和
                }
            }
            return sum;
        }

        /**
         * 更新单点值，并更新树状数组
         * @param row 行下标（原始矩阵下标，0-based）
         * @param col 列下标（原始矩阵下标，0-based）
         * @param val 新值
         */
        public void update(int row, int col, int val) {
            if (N == 0 || M == 0) {
                return;
            }
            int add = val - nums[row][col];  // 计算差值（变化量）
            nums[row][col] = val;            // 更新原始值

            // 更新树状数组：从当前点开始，逐层向上更新所有包含该点的区间
            for (int i = row + 1; i <= N; i += i & (-i)) {
                for (int j = col + 1; j <= M; j += j & (-j)) {
                    tree[i][j] += add;  // 将变化量累加到所有覆盖该点的区间
                }
            }
        }

        /**
         * 查询子矩阵[row1,col1]到[row2,col2]的和
         * @param row1 子矩阵左上角行下标
         * @param col1 子矩阵左上角列下标
         * @param row2 子矩阵右下角行下标
         * @param col2 子矩阵右下角列下标
         * @return 子矩阵和
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            if (N == 0 || M == 0) {
                return 0;
            }
            // 容斥原理：总面积 = 右下角总和 - 上方区域 - 左方区域 + 左上角重叠区域
            return sum(row2, col2) + sum(row1 - 1, col1 - 1) - sum(row1 - 1, col2) - sum(row2, col1 - 1);
        }
    }



}
