package com.example.design.leetcode.递归迭代.贿赂怪兽;

import java.util.Arrays;

public class MonsterBribe {
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
    public static int minCostToPassAllMonsters(int n, int[] a, int[] b) {
        // 边界条件判断
        if (n <= 0 || a == null || b == null || a.length != n || b.length != n) {
            return -1;
        }
        
        // 计算最大可能花费（所有怪兽都贿赂的总费用），作为DP数组的列边界
        int maxTotalCost = 0;
        for (int cost : b) {
            maxTotalCost += cost;
        }
        
        // dp[i][j]表示：通过前i只怪兽（0~i-1），花费不超过j时的最大能力值
        // 行：0~n（前0只到前n只），列：0~maxTotalCost（花费范围）
        int[][] dp = new int[n + 1][maxTotalCost + 1];
        
        // 初始化：通过前0只怪兽（没有怪兽）时，能力为0，花费为0
        // 第一行（i=0）全部初始化为0，因为int数组默认值为0，可省略显式初始化
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            // 当前处理的是第i-1号怪兽（0-indexed）
            int currentMonsterAbility = a[i - 1];
            int currentMonsterCost = b[i - 1];
            
            for (int j = 0; j <= maxTotalCost; j++) {
                // 初始状态：无法通过前i只怪兽
                dp[i][j] = Integer.MIN_VALUE;
                
                // 情况1：不贿赂第i-1号怪兽
                // 要求前i-1只怪兽的最大能力 >= 当前怪兽的能力，且前i-1只怪兽可通过
                if (dp[i - 1][j] >= currentMonsterAbility) {
                    dp[i][j] = dp[i - 1][j]; // 能力不变
                }
                
                // 情况2：贿赂第i-1号怪兽（如果费用足够）
                if (j >= currentMonsterCost) {
                    int prevCost = j - currentMonsterCost;
                    // 要求前i-1只怪兽在花费prevCost时可通过
                    if (dp[i - 1][prevCost] != Integer.MIN_VALUE) {
                        // 贿赂后能力 = 前i-1只的能力 + 当前怪兽的能力
                        int newAbility = dp[i - 1][prevCost] + currentMonsterAbility;
                        dp[i][j] = Math.max(dp[i][j], newAbility);
                    }
                }
            }
        }
        
        // 查找通过所有n只怪兽的最小花费，我们定义dp[i][j]为花费不超过j时所能获得的最大能力。这样定义的好处是，最后我们只需要遍历最后一行，找到第一个能力值不是Integer.MIN_VALUE的j，即为最小花费。
        for (int j = 0; j <= maxTotalCost; j++) {
            if (dp[n][j] != Integer.MIN_VALUE) {
                return j;
            }
        }
        
        // 无法通过所有怪兽
        return -1;
    }

    public static void main(String[] args) {
        // 测试用例1：全强制贿赂
        int n1 = 3;
        int[] a1 = {5, 6, 12};
        int[] b1 = {10, 20, 30};
        System.out.println("测试用例1结果：" + minCostToPassAllMonsters(n1, a1, b1)); // 预期60
        
        // 测试用例2：部分可选贿赂（不贿赂更优）
        int n2 = 2;
        int[] a2 = {3, 3};
        int[] b2 = {5, 10};
        System.out.println("测试用例2结果：" + minCostToPassAllMonsters(n2, a2, b2)); // 预期5
        
        // 测试用例3：多步选择影响后续成本
        int n3 = 4;
        int[] a3 = {2, 1, 5, 6};
        int[] b3 = {3, 1, 10, 20};
        System.out.println("测试用例3结果：" + minCostToPassAllMonsters(n3, a3, b3)); // 预期13
    }
}
    