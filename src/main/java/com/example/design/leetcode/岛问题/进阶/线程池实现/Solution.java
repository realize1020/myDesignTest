package com.example.design.leetcode.岛问题.进阶.线程池实现;

import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行计算加上并查集的思想来解决岛很大的问题
 *
 */
public class Solution {

    private static int width = 6;
    private static int height = 4;

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private AtomicInteger sum = new AtomicInteger();
    private ConcurrentHashMap<Integer,Integer> verticle = new ConcurrentHashMap<>();


    void infect(int a[][],int i,int j,int left,int right,ConcurrentHashMap<Integer,Integer> verticle)
    {
        if(i < 0||i >= height ||j < left|| j >= right || a[i][j] != 1)
            return;
        //处理边界感染,因为现在只根据宽度进行二等分，所以只需要处理宽度的边界就可以了
        int mid = left==0 ? right : left;
        int pivot = mid==right? mid-1 : mid;
        if (j == pivot) {
            //记录边界的坐标,记录垂直方向的坐标就行
            verticle.compute(i, (k, v) -> v == null ? 1 : v + 1);
        }
        a[i][j] = 2;
        infect(a,i-1,j,left,right,verticle);
        infect(a,i + 1,j,left,right,verticle);
        infect(a,i,j - 1,left,right,verticle);
        infect(a,i,j + 1,left,right,verticle);
    }

    int countIslands(int a[][]) throws ExecutionException, InterruptedException {
        int mid = width / 2;

        for(int i=0;i<width;i++){
            if(i % mid == 0){
                executorService.submit(new IslandRunnable(sum,i==mid?mid:0,i==mid?width:mid,a,verticle));
            }
        }
        executorService.shutdown(); // 不再接受新任务
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // 超时则强制终止
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        for(Map.Entry<Integer,Integer> map: verticle.entrySet()){
            if(map.getValue() > 1){
                sum.decrementAndGet();
            }
        }
        return sum.get();

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int a[][] = new int[][]{
                {0,0,1,0,1,0},
                {1,1,1,0,1,0},
                {1,0,1,1,0,0},
                {0,0,0,0,0,0}
        };
        System.out.println("岛屿数量:"+new Solution().countIslands(a));
    }

class IslandRunnable implements Runnable{

        private AtomicInteger sum = new AtomicInteger();

        private Integer left;
        private Integer right;

        private volatile int[][] a;

        private ConcurrentHashMap<Integer,Integer> verticle = new ConcurrentHashMap<>();

        public IslandRunnable(AtomicInteger sum,Integer left,Integer right,int[][] a,ConcurrentHashMap<Integer,Integer> verticle){
            this.sum = sum;
            this.left = left;
            this.right = right;
            this.a = a;
            this.verticle = verticle;
        }
        @Override
        public void run() {
            int num = 0;

            for (int i = 0; i < height; i++) {

                for (int j = left; j < right; j++) {

                    if (a[i][j] == 1) {
                        num++;
                        System.out.println(num);
                        infect(a, i, j,left,right,verticle);
                        //todo 边界处理要放在感染方法里去做，不然会导致边界处理错误，因为感染一次后，有可能边界的1就被改成2了，下一次就无法记录这个边界
                        //处理边界感染,因为现在只根据宽度进行二等分，所以只需要处理宽度的边界就可以了
//                        int mid = left==0 ? right : left;
//                        int pivot = mid==right? mid-1 : mid;
//                        if (j == pivot) {
//                            //记录边界的坐标,记录垂直方向的坐标就行
//                            verticle.compute(i, (k,v)->v==null ? 1 : v+1);
//                            System.out.println(verticle.isEmpty());
//                        }
                    }


                }
            }
            sum.addAndGet(num);
        }
}
}
