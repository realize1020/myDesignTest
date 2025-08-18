package com.example.design.leetcode.岛问题.进阶.completablefuture实现;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行计算加上并查集的思想来解决岛很大的问题
 *
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

    int countIslands(int a[][]) throws ExecutionException, InterruptedException {
       int mid = width / 2;
       AtomicInteger sum = new AtomicInteger();
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        CompletableFuture<HashSet<Integer>> leftFuture = completableFuture.supplyAsync(() -> {
                    int num = 0;
                    HashSet<Integer> verticle = new HashSet<>();
                    for (int i = 0; i < height; i++) {

                        for (int j = 0; j < mid; j++) {

                            if (a[i][j] == 1) {
                                num++;
                                infect(a, i, j);
                                //处理边界感染,因为现在只根据宽度进行二等分，所以只需要处理宽度的边界就可以了
                                if (j == mid-1) {
                                    //记录边界的坐标,记录垂直方向的坐标就行
                                    verticle.add(i);
                                }
                            }


                        }
                    }
                    sum.set(num);
                    return verticle;
                }
        );

        CompletableFuture<HashSet<Integer>> rightFutre = completableFuture.supplyAsync(() -> {
                    int num = 0;
                    HashSet<Integer> verticle = new HashSet<>();
                    for (int i = 0; i < height; i++) {

                        for (int j = mid; j < width; j++) {

                            if (a[i][j] == 1) {
                                num++;
                                infect(a, i, j);
                                //处理边界感染,因为现在只根据宽度进行二等分，所以只需要处理宽度的边界就可以了
                                if (j == mid-1) {
                                    //记录边界的坐标,记录垂直方向的坐标就行
                                    verticle.add(i);
                                }
                            }


                        }
                    }
                    sum.set(num);
                    return verticle;
                }
        );


        HashSet<Integer> leftSet = leftFuture.get();
        HashSet<Integer> rightSet = rightFutre.get();
        for (Integer i : leftSet) {
            if (rightSet.contains(i)) {
                sum.getAndDecrement();//减去重合的
            }
        }

        return sum.get();

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int a[][] = new int[][]{
//                {0,0,1,0,1,0},
//                {1,1,1,0,1,0},
//                {1,0,1,1,0,0},
//                {0,0,0,0,0,0}
                {0,0,1,0,1,0},
                {1,1,1,0,1,0},
                {1,0,1,1,0,0},
                {0,0,0,0,0,0}
        };
        System.out.println(new Solution().countIslands(a));
    }

//    @Data
//    static class isLand{
//        private int x;
//        private int y;
//    }
}
