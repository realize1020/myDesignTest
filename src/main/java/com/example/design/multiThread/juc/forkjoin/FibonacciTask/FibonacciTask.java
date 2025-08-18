package com.example.design.multiThread.juc.forkjoin.FibonacciTask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 计算斐波那契数列
 */
public class FibonacciTask extends RecursiveTask<Integer> {
    private final int n;

    public FibonacciTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        // 基线条件：n 很小，直接计算
        if (n <= 1) {
            return n;
        }
        // 创建子任务计算 fib(n-1)
        FibonacciTask fibMinus1 = new FibonacciTask(n - 1);
        fibMinus1.fork(); // 异步提交子任务

        // 创建子任务计算 fib(n-2) (可以在当前线程同步计算，或者也fork)
        FibonacciTask fibMinus2 = new FibonacciTask(n - 2);
        // int resultMinus2 = fibMinus2.compute(); // 在当前线程计算
        fibMinus2.fork(); // 或者也fork出去异步计算

        // 等待并获取子任务结果，然后合并
        return fibMinus2.join() + fibMinus1.join(); // 注意 join 顺序
        // 如果用了 fibMinus2.compute(), 则: return resultMinus2 + fibMinus1.join();
    }

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        int result = pool.invoke(new FibonacciTask(10)); // 计算 fib(10)
        System.out.println("Fib(10) = " + result); // 输出 55
    }
}