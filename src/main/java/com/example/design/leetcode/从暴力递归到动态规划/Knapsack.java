package com.example.design.leetcode.从暴力递归到动态规划;

/**
 * 1. getMaxValue 方法:•这是一个公共方法，接收三个参数：物品的重量数组 w[]、物品的价值数组 v[] 和背包的最大承重 bag。
 * •调用私有辅助方法 process 来计算可以获得的最大价值，初始时从第一个物品开始考虑，已使用的重量为0。
 * 2. process 方法:•这是一个递归方法，用于计算从当前索引 index 开始，已经使用的重量为 alreadyW 的情况下，可以获得的最大价值。
 * •如果当前已使用的重量超过了背包的容量，则返回 -1 表示这种方案不可行。
 * •如果所有物品都已经被考虑过（即 index == w.length），则返回0，因为没有更多的物品可以加入背包。
 * •p1 表示不选择当前物品，直接跳到下一个物品所能获得的最大价值。
 * •p2next 表示选择当前物品后，继续考虑剩余物品所能获得的最大价值。
 * 如果选择了当前物品但导致了不可行的方案（即超过背包容量），则 p2next 会是 -1，此时 p2 设为 -1。
 * •最终，该方法返回选择或不选择当前物品两种情况下的最大值。
 */
public class Knapsack {

	public static int getMaxValue(int[] w, int[] v, int bag) {
		return process(w, v, 0, 0, bag);
	}

	// index... 最大价值
	public static int process(int[] w, int[] v, int index, int alreadyW, int bag) {
		if (alreadyW > bag) {
			return -1;
		}
		// 重量没超
		if (index == w.length) {
			return 0;
		}
		int p1 = process(w, v, index + 1, alreadyW, bag);
		int p2next = process(w, v, index + 1, alreadyW + w[index], bag);
		int p2 = -1;
		if (p2next != -1) {
			p2 = v[index] + p2next;
		}
		return Math.max(p1, p2);

	}

	public static int maxValue(int[] w, int[] v, int bag) {
		return process(w, v, 0, bag);// 调用递归函数 process，从第0个物品开始考虑，剩余容量为 bag
	}

    /**
     * 递归计算从当前索引 index 开始，在剩余容量 rest 的情况下可以获得的最大价值。
     *这个递归算法通过枚举每一种可能的选择（选择或不选择当前物品），并递归地计算在不同选择下的最大价值，最终返回最优解。
     * 尽管这种方法直观易懂，但由于存在大量的重复计算，对于较大的输入数据集效率较低。为了提高效率，可以采用动态规划或记忆化搜索来优化该算法。
     * @param w     物品重量数组
     * @param v     物品价值数组
     * @param index 当前考虑的物品索引
     * @param rest  剩余的背包容量
     * @return 可以获得的最大价值
     */
	public static int process(int[] w, int[] v, int index, int rest) {
        // 如果剩余容量小于0，说明当前选择不可行，返回-1
		if (rest < 0) { // base case 1
			return -1;
		}
        // 如果所有物品都已考虑过（即 index == w.length），返回0，因为没有更多的物品可以加入背包
		if (index == w.length) { // base case 2
			return 0;
		}
        // 不选择当前物品，直接跳到下一个物品所能获得的最大价值
		int p1 = process(w, v, index + 1, rest);

        // 选择当前物品后，继续考虑剩余物品所能获得的最大价值
		int p2 = -1;
		int p2Next = process(w, v, index + 1, rest - w[index]);
        // 如果选择了当前物品但导致了不可行的方案（即超过背包容量），则 p2Next 会是 -1
        // 否则，加上当前物品的价值 v[index]
		if(p2Next!=-1) {
			p2 = v[index] + p2Next;
		}
        // 返回不选择当前物品和选择当前物品两种情况下的最大值
		return Math.max(p1, p2);
	}

	public static int dpWay(int[] w, int[] v, int bag) {
		int N = w.length;
		int[][] dp = new int[N + 1][bag + 1];
		// dp[N][...] = 0
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= bag; rest++) { // rest < 0
				int p1 = dp[index+1][rest];
				int p2 = -1;
				if(rest - w[index] >= 0) {//当前物品可以放入背包：rest >= w[index]
					p2 = v[index] + dp[index + 1][rest - w[index]];
				}
				dp[index][rest] = Math.max(p1, p2);
			}
		}
		return dp[0][bag];
	}


	public static int dpWay2(int[] w, int[] v, int bag){
        // 获取物品的数量 N
        int N = w.length;

        // 创建一个二维数组 dp，大小为 (N+1) x (bag+1)
        // dp[i][j] 表示从前 i 个物品中选择，背包容量为 j 时可以获得的最大价值
        int[][] dp = new int[N + 1][bag + 1];

        // 初始化 dp 数组的边界条件：当没有物品可选时（即 index == N），最大价值为 0
        // 因此 dp[N][...] = 0，这部分已经在初始化 dp 数组时默认设置为 0

        // 从最后一个物品开始向前遍历
        for (int index = N - 1; index >= 0; index--) {
            // 对于每一个可能的剩余容量 rest，从 0 到 bag
            for (int rest = 0; rest <= bag; rest++) {
                // p1 表示不选择当前物品（index）时的最大价值
                // 相当于将问题转化为前 index+1 个物品在容量为 rest 的情况下的最大价值
                int p1 = dp[index + 1][rest];

                // p2 表示选择当前物品（index）时的最大价值
                // 首先检查当前物品是否可以放入背包（即 rest >= w[index]）
                int p2 = -1;
                if (rest >= w[index]) { // 当前物品可以放入背包
                    // 如果可以放入，则加上当前物品的价值 v[index] 和剩余容量 rest - w[index] 下的最大价值
                    p2 = v[index] + dp[index + 1][rest - w[index]];
                }

                // 取不选择和选择当前物品两种情况下的最大值
                dp[index][rest] = Math.max(p1, p2);
            }
        }

        // 返回 dp[0][bag]，即从前 0 个物品开始考虑，背包容量为 bag 时可以获得的最大价值
        return dp[0][bag];
    }

    /**
     * 用贪心算法求解0-1背包问题的步骤是，首先计算每种物品单位重量的价值vi/wi；
     * 然后，将物品的vi/wi的大小进行降序进行排列，依贪心选择策略，将尽可能多的单位重量价值最高的物品装入背包。
     * 若将这种物品全部装入背包后，背包内的物品总量未超过c，则选择单位重量价值次高的物品并尽可能多地装入背包。
     * 依此策略一直进行下去，直到背包装满为止。
     * @param w
     * @param v
     * @param bag
     * @return
     */
    public static int violence(int[] w,int[] v,int bag){
        return 0;
    }

	public static void main(String[] args) {
		int[] weights = {2, 3};
		int[] values = { 2, 3};
		int bag = 4;
		System.out.println(getMaxValue(weights, values, bag));
		//System.out.println(maxValue(weights, values, bag));
		//System.out.println(dpWay(weights, values, bag));
	}

}
