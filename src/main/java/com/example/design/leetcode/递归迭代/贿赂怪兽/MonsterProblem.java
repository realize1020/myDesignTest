package com.example.design.leetcode.递归迭代.贿赂怪兽;

import java.util.Arrays;

public class MonsterProblem {

    /**
     * 使用动态规划计算通过所有怪兽的最小花费
     * 循环设计说明（为什么这样设计更合理）
     * 外层循环 i（1 到 n）的含义
     * i 表示 "已经处理的怪兽数量"（从 1 个到 n 个），对应逻辑上的 "前 i 个怪兽"
     * 由于怪兽数组是 0-indexed（0 到 n-1），所以第 i 次循环实际处理的是第i-1号怪兽
     * 这样设计可以自然兼容 "前 0 个怪兽" 的初始状态（i=0 时，能力为 0，花费 0）
     * 内层循环 j（0 到 m）的含义
     * j 表示 "累计花费的上限"（从 0 到最大总花费 m）
     * 从 0 开始遍历的原因是：我们需要找到最小花费，必须从最低成本开始检查
     * 每个 j 对应的状态是 "在不超过 j 的花费下，能达到的最大能力"
     * 为什么不能合并或调换循环
     * 必须先处理完i-1个怪兽的所有花费状态，才能计算i个怪兽的状态（依赖前序结果）
     * 若调换循环顺序（先 j 后 i），会导致计算i时i-1的状态尚未完全计算，结果错误
     * 优化点说明
     * 循环逻辑统一
     * 外层循环明确处理 "怪兽数量递增"，内层循环处理 "花费递增"，两者职责清晰
     * 通过currentA和currentB临时变量，避免重复计算a[i-1]和b[i-1]，提升可读性
     * 初始化优化
     * 使用Arrays.fill批量初始化，比嵌套循环更简洁
     * 明确区分 "前 0 个怪兽"（有效状态）和 "前 i≥1 个怪兽"（初始不可达）
     * 保留核心优势
     * 维持原有的动态规划思路，时间复杂度仍为 O (n×m)
     * 循环结构更符合 "从小规模子问题逐步解决大规模问题" 的动态规划思想
     * @param n 怪兽数量
     * @param a 怪兽的能力值数组（0-indexed）
     * @param b 怪兽的贿赂费用数组（0-indexed）
     * @return 通过所有怪兽的最小花费，如果无法通过所有怪兽则返回-1
     */
    public static int computeMinCost(int n, int[] a, int[] b) {
        // 计算所有怪兽的贿赂费用之和，作为花费上限
        int m = 0;
        for (int money : b) {
            m += money;
        }
        
        // dp[i][j] 表示考虑前i个怪兽，花费不超过j元时所能获得的最大能力值
        // 如果dp[i][j] == Integer.MIN_VALUE，表示无法在花费j元内通过前i个怪兽
        int[][] dp = new int[n + 1][m + 1];
        
        // 初始化第一行：没有怪兽时，能力值为0，花费为0
        // Java数组默认初始化为0，所以dp[0][j]对于所有j都是0
        // 但为了清晰，我们可以显式初始化
        Arrays.fill(dp[0], 0);
        
        // 初始化其他行为最小负值，表示初始状态不可达
        for (int i = 1; i <= n; i++) {
            Arrays.fill(dp[i], Integer.MIN_VALUE);
        }
        
        // 动态规划处理每个怪兽
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                // 情况1：不贿赂第i个怪兽
                // 如果前i-1个怪兽在花费j下的最大能力值 >= 第i个怪兽的能力值
                // 则可以直接击败第i个怪兽，能力值不变
                if (dp[i - 1][j] >= a[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                }
                
                // 情况2：贿赂第i个怪兽
                // 需要满足两个条件：
                // 1. 当前花费j >= 第i个怪兽的贿赂费用
                // 2. 前i-1个怪兽在花费j-b[i-1]下是可达的
                if (j >= b[i - 1] && dp[i - 1][j - b[i - 1]] != Integer.MIN_VALUE) {
                    // 贿赂后，能力值增加a[i-1]
                    int newAbility = dp[i - 1][j - b[i - 1]] + a[i - 1];
                    // 取两种情况中的最大值（如果情况1已经设置了一个值，则取较大者）
                    if (dp[i][j] < newAbility) {
                        dp[i][j] = newAbility;
                    }
                }
            }
        }
        /** 寻找最小花费：遍历所有可能的花费，找到第一个能够通过所有怪兽的花费，我们定义dp[i][j]为花费不超过j时所能获得的最大能力。这样定义的好处是，最后我们只需要遍历最后一行，找到第一个能力值不是Integer.MIN_VALUE的j，即为最小花费。
         *  然而，如果我们定义dp[i][j]为花费恰好为j时的最大能力，那么我们需要在最后遍历所有可能的花费，找到最小的j使得dp[n][j]不是负无穷。
         *  两种方式都是可行的。但之前实现的方式是“不超过j”，所以最后一行是单调的（因为花费越多，能力值不会变小，所以第一个非负无穷就是最小花费）
         */
        for (int j = 0; j <= m; j++) {
            if (dp[n][j] != Integer.MIN_VALUE) {
                return j;
            }
        }
        
        // 如果无法通过所有怪兽，返回-1
        return -1;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1：简单情况
        int n1 = 2;
        int[] a1 = {10, 5};
        int[] b1 = {5, 3};
        int result1 = computeMinCost(n1, a1, b1);
        System.out.println("测试用例1 - 最小花费: " + result1); // 预期输出: 5
        
        // 测试用例2：需要贿赂所有怪兽
        int n2 = 3;
        int[] a2 = {5, 10, 15};
        int[] b2 = {2, 3, 4};
        int result2 = computeMinCost(n2, a2, b2);
        System.out.println("测试用例2 - 最小花费: " + result2); // 预期输出: 9 (2+3+4)
        
        // 测试用例3：无法通过所有怪兽
        int n3 = 2;
        int[] a3 = {10, 20};
        int[] b3 = {5, 10};
        int result3 = computeMinCost(n3, a3, b3);
        System.out.println("测试用例3 - 最小花费: " + result3); // 预期输出: -1 (初始能力0<10，贿赂第一个后能力10<20)
        
        // 测试用例4：混合情况
        int n4 = 4;
        int[] a4 = {3, 2, 5, 4};
        int[] b4 = {1, 2, 3, 2};
        int result4 = computeMinCost(n4, a4, b4);
        System.out.println("测试用例4 - 最小花费: " + result4); // 预期输出: 4 (有多种可能方案)
    }
}