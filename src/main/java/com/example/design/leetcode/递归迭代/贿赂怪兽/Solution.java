package com.example.design.leetcode.递归迭代.贿赂怪兽;
// 贿赂怪兽
// 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
// 如果你当前的能力小于i号怪兽的能力，则必须付出b[i]的钱贿赂这个怪兽
// 然后怪兽就会加入你，他的能力a[i]直接累加到你的能力上
// 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
// 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
// 返回通过所有的怪兽，需要花的最小钱数
// 测试链接 : https://www.nowcoder.com/practice/736e12861f9746ab8ae064d4aae2d5a9
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//输入描述：
//第一行输入一个整数N，表示你的能力值N<=500
//接下来N行，每行输入两个整数，表示怪兽的力量和需要的金钱
//输出描述：
//输出一个整数，需要花的最小钱数
//示例1
//输入：
//2
//8 10
//6 5
//复制
//输出：
//10
public class Solution {
    //讲解本题的目的不仅仅是为了通过这个题，而是进行如下的思考:
    // 假设a[i]数值的范围很大，但是b[i]数值的范围不大，该怎么做？
    // 假设a[i]数值的范围不大，但是b[i]数值的范围很大，又该怎么做？


    // 假设a[i]数值的范围很大，但是b[i]数值的范围不大
    // 时间复杂度O(n * 所有怪兽的钱数累加和)
/*    public static int compute1(int n, int[] a, int[] b){//a是能力数组，b是钱花费数组
        int sum = f(n, a, b);
        return sum;
    }

    private static int f(int n, int[] a, int[] b) {
        int sum = 0;
        for(int i=0;i<a.length;i++){
            if(n >=a[i]) {
                //如果能通过，也需要考虑这次贿赂会不会导致下次不用贿赂就能通过
                int spend=f(n + a[i], a[i], sum+b[i]);//如果这次贿赂了
                int notSpend=f(n, a[i], sum);//如果这次不贿赂
                if(spend == Math.min(spend, notSpend)){//判断贿赂和直接通过哪个更小
                    sum =  sum+b[i];
                    n=n+a[i];
                }
            }else{
                sum = sum+b[i];//如果不能通过，则直接贿赂
                n=n+a[i];
            }
        }
        return sum;
    }

    private static int f(int n, int a, int b) {
        if(n >=a){
            return b;
        }else{
            n=n+a;
            return Integer.MAX_VALUE;
        }
    }*/

//    public static long compute1(int n, int[] a, int[] b){//a是能力数组，b是钱花费数组
//        long sum = f(n, a, b,0);
//        return sum;
//    }
//
//    private static long f(int n, int[] a, int[] b,int x) {
//        long sum = 0;
//        //sum=f(n, 0, 0,x,a,b);
//        if(n >=a[x]) {
//            //如果能通过，也需要考虑这次贿赂会不会导致下次不用贿赂就能通过
//            long spend=f(n + a[x], a[x], sum+b[x],x+1,a,b);//如果这次贿赂了
//            long notSpend=f(n, a[x], sum,x+1,a,b);//如果这次不贿赂
//            if(spend == Math.min(spend, notSpend)){//判断贿赂和直接通过哪个更小
//                sum =  sum+b[x];
//            }
//        }else{
//            sum = sum+b[x];//如果不能通过，则直接贿赂
//        }
////        for(int i=0;i<a.length;i++){
////            if(n >=a[i]) {
////                //如果能通过，也需要考虑这次贿赂会不会导致下次不用贿赂就能通过
////                int spend=f(n + a[i], a[i], sum+b[i],i+1,a,b);//如果这次贿赂了
////                int notSpend=f(n, a[i], sum,i+1,a,b);//如果这次不贿赂
////                if(spend == Math.min(spend, notSpend)){//判断贿赂和直接通过哪个更小
////                    sum =  sum+b[i];
////                    n=n+a[i];
////                }
////            }else{
////                sum = sum+b[i];//如果不能通过，则直接贿赂
////                n=n+a[i];
////            }
////        }
//
//        return sum;
//    }
//
//    private static long f(int n, int a, long b,int x,int []a2,int []b2) {
//        long sum = b;
//        if(n < a){
//            return Integer.MAX_VALUE;
//        }else{
//            for(int i=x;i<a2.length;i++){
//                long spend=f(n + a2[i], a2[i], b+b2[i],i+1,a2,b2);//如果这次贿赂了
//                long notSpend=f(n, a2[i], b,i+1,a2,b2);//如果这次不贿赂
//                if(spend == Math.min(spend, notSpend)){//判断贿赂和直接通过哪个更小
//                    sum =  sum+b2[i];
//                }
//
//            }
//        }
//        return sum;
//    }


    public static long compute2(int n, int[] a, int[] b) {
        long sum = minCost(n, 0, 0,a,b);
        return sum;
    }




    /** 递归写法
     * 正确的处理流程应该是：
     * 检查是否处理完所有怪兽（终止条件）
     * 如果没处理完，检查当前能力是否足够通过当前怪兽
     * 如果不够，必须贿赂
     * 如果足够，可以选择贿赂或不贿赂，取最小值
     * @param index
     * @param currentAbility
     * @param n
     * @param a
     * @param b
     * @return
     */
    private static long minCost(int index, long currentAbility, int n, int[] a, int[] b) {
        if (index == n) return 0; // 处理完所有怪兽作为递归终止条件

        if (currentAbility < a[index]) {
            // 必须贿赂
            return b[index] + minCost(index + 1, currentAbility + a[index], n, a, b);
        } else {
            // 可以选择贿赂或不贿赂
            long bribe = b[index] + minCost(index + 1, currentAbility + a[index], n, a, b);
            long notBribe = minCost(index + 1, currentAbility, n, a, b);
            return Math.min(bribe, notBribe);
        }
    }


    /**
     * 正常递归写法
     * @param d
     * @param p
     * @param ability
     * @param index
     * @return
     */
    //目前你的能力是ability，你来到了index号怪兽面前，如果要通过后序所有的怪兽，请返回需要花的最少钱数。
    public static long process1(int[] d, int[] p, int ability, int index) {
        if (index == d.length) {
            return 0;
        }
        if (ability < d[index]) {
            return p[index] + process1(d, p, ability + d[index], index + 1);
        } else {//ability>=d[index] 可以贿赂，也可以不贿赂
            return Math.min(
                    p[index] + process1(d, p, ability + d[index], index + 1),
                    0 + process1(d, p, ability, index + 1)
            );
        }
    }

    public static long fun1(int[] d, int[] p) {
        return process1(d, p, 0, 0);
    }



    //讲解本题的目的不仅仅是为了通过这个题，而是进行如下的思考:
    // 假设a[i]数值的范围很大，但是b[i]数值的范围不大，该怎么做？
    // 假设a[i]数值的范围不大，但是b[i]数值的范围很大，又该怎么做？
/*    public static long process2(int[] a ,int [] b,int n){
        int N = a.length;
        int M = b.length;
        int sum = 0;
        int [][] dp =new int[N+1][M+1];
        for(int i=0;i<N;i++)
            if (n > dp[i][i]) {
                //能力值比怪兽大，可以贿赂
                sum = b[i]+dp[i+1][i+1];
            }
    }*/
//从0...index号怪兽花的钱必须严格==money
    //如果通过不了返回-1
    //如果可以通过，返回能通过情况下的最大能力值
    public static long process2(int[] d, int[] p, int index, int money) {
        if (index == -1) {//一个怪兽也没有遇到
            return money == 0 ? 0 : -1;
        }
        //index >=0
        //1>不贿赂当前index号怪兽
        long preMaxAbility = process2(d, p, index - 1, money);
        long p1 = -1;
        if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
            p1 = preMaxAbility;
        }
        //2>贿赂当前的怪兽，当前的钱p[index]
        long preMaxAbility2 = process2(d, p, index - 1, money - p[index]);
        long p2 = -1;
        if (preMaxAbility2 != -1) {
            p2 = d[index] + preMaxAbility2;
        }
        return Math.max(p1, p2);
    }

    public static int minMoney(int[] d, int[] p) {
        int allMoney = 0;
        for (int i = 0; i < p.length; i++) {
            allMoney += p[i];
        }
        int N = d.length;
        for (int money = 0; money < allMoney; money++) {
            if (process2(d, p, N - 1, money) != -1) {
                return money;
            }
        }
        return allMoney;
    }


    public static int compute1(int n, int[] a, int[] b) {
        int m = 0;//m 是所有怪兽价格之和，作为花费上限。
        for (int money : b) {
            m += money;
        }
        // dp[i][j] : 花的钱不能超过j，通过前i个怪兽，最大能力是多少
        // 如果dp[i][j] == Integer.MIN_VALUE
        // 表示花的钱不能超过j，无论如何都无法通过前i个怪兽
        //状态转移考虑两种情况：不买第 i 个怪兽 或 买下它（如果钱够且之前状态可达）。
        //最终返回能达成有效能力的最小花费。
        int[][] dp = new int[n + 1][m + 1];//？为什么是n+1
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = Integer.MIN_VALUE;
//                if (dp[i - 1][j] >= a[i]) {//不贿赂怪兽 i，如果之前的能力值 dp[i-1][j] 已经足够击败怪兽 i，则可以直接通过，能力值不变
//                    dp[i][j] = dp[i - 1][j];//当前能力值不变
//                }
//                if (j - b[i] >= 0 && dp[i - 1][j - b[i]] != Integer.MIN_VALUE) {//贿赂怪兽 i
//                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - b[i]] + a[i]);//？
//                }
                // 正确访问第i-1号怪兽（i从1到n）
                if (dp[i - 1][j] >= a[i-1]) {  // 不贿赂当前怪兽（i-1号）
                    dp[i][j] = dp[i - 1][j];
                }
                if (j - b[i-1] >= 0 && dp[i - 1][j - b[i-1]] != Integer.MIN_VALUE) {  // 贿赂当前怪兽（i-1号）
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - b[i-1]] + a[i-1]);//如果当前花费j至少为贿赂费用b[i]（即j - b[i] >= 0），且前i-1个怪兽在花费j - b[i]下可达（即dp[i-1][j - b[i]] != Integer.MIN_VALUE），则可以选择贿赂第i个怪兽。贿赂后，能力值增加a[i]，因此dp[i][j]更新为max(dp[i][j], dp[i-1][j - b[i]] + a[i])（即取不贿赂和贿赂中的最大能力值）。
                }
            }
        }
        int ans = -1;
        for (int j = 0; j <= m; j++) {
            if (dp[n][j] != Integer.MIN_VALUE) {
                ans = j;
                break;
            }
        }
        return ans;
    }

    /**
     * 自己修改for循环的起始点
     * @param n
     * @param a
     * @param b
     * @return
     */
    public static int my(int n, int[] a, int[] b) {
        int m = 0;//m 是所有怪兽价格之和，作为花费上限。
        for (int money : b) {
            m += money;
        }
        // dp[i][j] : 花的钱不能超过j，通过前i个怪兽，最大能力是多少
        // 如果dp[i][j] == Integer.MIN_VALUE
        // 表示花的钱不能超过j，无论如何都无法通过前i个怪兽
        //状态转移考虑两种情况：不买第 i 个怪兽 或 买下它（如果钱够且之前状态可达）。
        //最终返回能达成有效能力的最小花费。
        int N = n+1;
        int M = m+1;
        int[][] dp = new int[N][M];//？为什么是n+1
        for(int i=1;i<=N-1;i++){//todo 这里i是从1开始
            for(int j=0;j<=m;j++){
                dp[i][j] = Integer.MIN_VALUE;
            }
        }
        for(int i=0;i<N-1;i++){
            for(int j=0;j<=m;j++){//todo 这里修改
               // dp[i][j] = Integer.MIN_VALUE;
                if(dp[i][j]>a[i]){//不贿赂
                    dp[i + 1][j] = Math.max(dp[i + 1][j], dp[i][j]);
                }
                if(j+b[i] <= m && dp[i][j] != Integer.MIN_VALUE){//如果当前花费j ≥ 当前怪兽的贿赂费用，并且前i-1个怪兽在花费j-b[i]下可达，
                    dp[i+1][j + b[i]] = Math.max(dp[i+1][j+b[i]],dp[i][j]+a[i]);//todo 这里修改
                }
            }
        }
        int ans = -1;
        for (int j = 0; j <= m; j++) {
            if (dp[n][j] != Integer.MIN_VALUE) {
                return j; //todo 这里修改

            }
        }
        return ans;
    }

    /**
     * ai
     * @param n
     * @param a
     * @param b
     * @return
     */

    public static int computeMinCost(int n, int[] a, int[] b) {
        // 计算所有怪兽的贿赂费用之和，作为花费上限
        int totalCost = 0;
        for (int cost : b) {
            totalCost += cost;
        }

        // dp[i][j] 表示考虑前i个怪兽，花费不超过j元时所能获得的最大能力值
        // 注意：这里i和j都从0开始计数
        int[][] dp = new int[n + 1][totalCost + 1];

        // 初始化：没有怪兽时，能力值为0
        for (int j = 0; j <= totalCost; j++) {
            dp[0][j] = 0;
        }

        // 初始化其他行为最小负值，表示初始状态不可达
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= totalCost; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }

        // 动态规划处理每个怪兽
        // 注意：这里i从0开始，表示处理第i个怪兽（0-indexed）
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= totalCost; j++) {
                // 情况1：不贿赂当前怪兽
                if (dp[i][j] >= a[i]) {
                    dp[i + 1][j] = Math.max(dp[i + 1][j], dp[i][j]);
                }

                // 情况2：贿赂当前怪兽
                if (j + b[i] <= totalCost && dp[i][j] != Integer.MIN_VALUE) {
                    dp[i + 1][j + b[i]] = Math.max(
                            dp[i + 1][j + b[i]],
                            dp[i][j] + a[i]
                    );
                }
            }
        }

        // 寻找最小花费：遍历所有可能的花费，找到第一个能够通过所有怪兽的花费
        for (int j = 0; j <= totalCost; j++) {
            if (dp[n][j] != Integer.MIN_VALUE) {
                return j;
            }
        }

        // 如果无法通过所有怪兽，返回-1
        return -1;
    }



    // 假设a[i]数值的范围不大，但是b[i]数值的范围很大
    // 时间复杂度O(n * 所有怪兽的能力累加和)
    //不贿赂：如果之前就能达到能力值 j，且 j 足够击败当前怪兽，则现在也能达到能力值 j
    //贿赂：如果之前能达到 (j - 当前怪兽能力值)，则通过贿赂当前怪兽也能达到能力值 j
    public static int compute3(int n, int[] a, int[] b) {
        int m = 0;
        for (int ability : a) {
            m += ability;
        }
        // dp[i][j] : 能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
        // 如果dp[i][j] == Integer.MAX_VALUE
        // 表示能力正好是j，无论如何都无法通过前i个怪兽
        int[][] dp = new int[n + 1][m + 1];

        // 初始化：不选任何怪兽，能力值为0时花费为0
        dp[0][0] = 0;
        for (int j = 1; j <= m; j++) {
            dp[0][j] = Integer.MAX_VALUE;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                /**
                 * dp[i - 1][j] != Integer.MAX_VALUE：表示在考虑前 i-1 个怪兽时，拥有能力值 j 是可达的状态（不是无法达到的状态）
                 * j >= a[i-1]：表示当前拥有的能力值 j 足够强大，可以击败第 (i-1) 号怪兽（因为怪兽编号从0开始）
                 * 如果这两个条件都满足，说明我们可以不贿赂第 (i-1) 号怪兽直接通过它，此时能力值保持为 j 不变
                 * 因此 dp[i][j] = dp[i - 1][j]，表示获得能力值 j 所需的最小花费与之前相同
                 */
                // 不贿赂第(i-1)号怪兽（注意数组索引）
                if (dp[i - 1][j] != Integer.MAX_VALUE && j >= a[i-1]) {
                    dp[i][j] = dp[i - 1][j];
                }

                // 贿赂第(i-1)号怪兽
                if (j - a[i-1] >= 0 && dp[i - 1][j - a[i-1]] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - a[i-1]] + b[i-1]);
                }
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int j = 0; j <= m; j++) {
            ans = Math.min(ans, dp[n][j]);
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }




    public static void main(String[] args) {
        // 测试用例1: 题目示例
        // 输入: 能力值2, 怪兽[(8,10), (6,5)]
        // 预期输出: 10
//        System.out.println("测试用例1:");
//        System.out.println(compute1(2, new int[]{8,6}, new int[]{10,5})); // 应该输出10

        // 测试用例2: 所有怪兽都需要贿赂的情况
        // 输入: 能力值0, 怪兽[(5,3), (10,7), (15,2)]
        // 预期输出: 10 (必须贿赂所有怪兽)
//        System.out.println("测试用例2:");
//        System.out.println(compute1(0, new int[]{5,10,15}, new int[]{3,7,2})); // 应该输出12

        // 测试用例3: 可以全部直接通过的情况
        // 输入: 能力值20, 怪兽[(5,3), (10,7), (15,2)]
        // 预期输出: 0 (无需贿赂任何怪兽)
//        System.out.println("测试用例3:");
//        System.out.println(compute1(20, new int[]{5,10,15}, new int[]{3,7,2})); // 应该输出0

        // 测试用例4: 部分需要贿赂的情况
        // 输入: 能力值5, 怪兽[(10,5), (3,2), (15,8)]
        // 预期输出: 5 (贿赂第1个怪兽)
//        System.out.println("测试用例4:");
//        System.out.println(compute1(5, new int[]{10,3,15}, new int[]{5,2,8})); // 应该输出5

        // 测试用例5: 空数组情况
        // 输入: 能力值5, 空怪兽数组
        // 预期输出: 0 (没有怪兽需要通过)
//        System.out.println("测试用例5:");
//        System.out.println(compute1(5, new int[]{}, new int[]{})); // 应该输出0

        // 测试用例6: 复杂策略情况
        // 输入: 能力值1, 怪兽[(5,100), (2,1), (10,50)]
        // 可能需要先贿赂获得能力再通过后续怪兽
//        System.out.println("测试用例6:");
//        System.out.println(compute1(1, new int[]{5,2,10}, new int[]{100,1,50}));

        /**
         * 测试用例 3：多步选择（中间贿赂影响后续成本）
         * 输入：
         * n = 0
         * a = [3, 2, 5, 6, 7, 9]
         * b = [5, 1, 10, 15, 20, 100]
         */

        System.out.println("测试用例7:");
        System.out.println(compute3(6, new int[]{3, 2, 5, 6, 7, 9}, new int[]{5, 1, 10, 15, 20, 100}));
        //System.out.println(fun1( new int[]{3, 2, 5, 6, 7, 9}, new int[]{5, 1, 10, 15, 20, 100}));
        //System.out.println(compute1(0, new int[]{3, 2}, new int[]{5, 1}));
    }

}
