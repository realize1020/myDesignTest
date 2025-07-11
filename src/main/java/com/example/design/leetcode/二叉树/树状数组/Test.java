package com.example.design.leetcode.二叉树.树状数组;

/**
 * 手动写二维indexTree，测试
 */
public class Test {
    public static void main(String[] args) {
        int [][]arr ={
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}};

        /*int M = arr.length;
        int N = arr[0].length;

        int [][]num =new int[M][N];



        int [][] tree =new int [M+1][N+1];
        for(int k=0;k<M;k++){
            for(int l=0;l<N;l++){
                int add = arr[k][l]-num[k][l];
                num[k][l] = arr[k][l];
                int i =k+1;
                while(i<=M){
                   int j=l+1;
                    while(j<=N){
                        tree[i][j]+=add;
                        j+=j&(-j);
                    }
                    i+=i&(-i);
                }
            }
        }

        printResult(tree);*/

        //不使用动态更细的话，不需要创建num数组
        int M = arr.length;
        int N = arr[0].length;
        int[][] tree = new int[M + 1][N + 1];

        for (int k = 0; k < M; k++) {
            for (int l = 0; l < N; l++) {
                int i = k + 1;
                int j0 = l + 1;

                while (i <= M) {
                    int j = j0;
                    while (j <= N) {
                        tree[i][j] += arr[k][l];  // 直接累加原始值
                        j += j & (-j);
                    }
                    i += i & (-i);
                }
            }
        }

        printResult(tree);
    }

    private static void printResult(int[][] tree) {
        for(int i=0;i<tree.length;i++){
            for(int j=0;j<tree[0].length;j++){
                System.out.print(tree[i][j]+" ");
            }
            System.out.println();
        }
    }
}
