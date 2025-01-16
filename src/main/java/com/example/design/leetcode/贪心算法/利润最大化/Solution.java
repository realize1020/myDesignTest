package com.example.design.leetcode.贪心算法.利润最大化;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Solution {
    /**
     *这个问题的目标是在给定初始资本 W 的情况下，选择最多 K 个项目进行投资，以最大化最终的资本。
     * @param K
     * @param W
     * @param Profits
     * @param Capital
     * @return
     */
    public static int findMaximizedCapital(int K, int W, int[] Profits, int[] Capital) {
        // 创建两个优先队列：一个按启动资本升序排序，另一个按利润降序排序
        PriorityQueue<Program> minCostQ = new PriorityQueue<Program>(new MinCostComparator());
        PriorityQueue<Program> maxProfitQ = new PriorityQueue<Program>(new MaxProfitComparator());
        // 将所有项目按照启动资本加入到 minCostQ 中
        for (int i = 0; i < Profits.length; i++) {
            minCostQ.add(new Program(Profits[i], Capital[i]));
        }
        // 执行最多 K 次投资
        for (int i = 0; i < K; i++) {
            // 将所有当前资本可以启动的项目从 minCostQ 移动到 maxProfitQ
            while (!minCostQ.isEmpty() && minCostQ.peek().c <= W) {
                maxProfitQ.add(minCostQ.poll());
            }
            // 如果没有可投资的项目了，返回当前资本
            if (maxProfitQ.isEmpty()) {
                return W;
            }
            // 投资利润最高的项目，并更新当前资本
            W += maxProfitQ.poll().p;
        }
        // 返回最终资本
        return W;
    }

    public static class Program {
        public int p;// 利润
        public int c;// 启动资本

        public Program(int p, int c) {
            this.p = p;
            this.c = c;
        }
    }

    public static class MinCostComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            //return o1.c - o2.c;
            return Integer.compare(o1.c, o2.c); // 使用 Integer.compare 来避免整数溢出
        }

    }

    public static class MaxProfitComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
        //    return o2.p - o1.p;
            return Integer.compare(o2.p,o1.p);
        }

    }

    public static void main(String[] args) {
        //int maximizedCapital = findMaximizedCapital(2, 0, new int[]{1, 2, 3}, new int[]{0, 1, 1});
        int maximizedCapital = findMaximizedCapital(2, 0, new int[]{1, 2, 3}, new int[]{0, 1, 1});
        System.out.println("所需最大资本："+maximizedCapital);
    }
}
