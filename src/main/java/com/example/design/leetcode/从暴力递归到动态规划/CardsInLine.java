package com.example.design.leetcode.从暴力递归到动态规划;

/**
 * 给定一个整型数组arr，代表数值不同的纸牌排成一条线
 * 玩家A和玩家B依次拿走每张纸牌
 * 规定玩家A先拿，玩家B后拿
 * 但是每个玩家每次只能拿走最左或最右的纸牌
 * 玩家A和玩家B都绝顶聪明
 * 请返回最后获胜者的分数
 */
public class CardsInLine {

	public static int win1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		return Math.max(f(arr, 0, arr.length - 1), s(arr, 0, arr.length - 1));
	}

	public static int f(int[] arr, int L, int R) {
		if (L == R) {
			return arr[L];
		}
		return Math.max(arr[L] + s(arr, L + 1, R), arr[R] + s(arr, L, R - 1));
	}

	public static int s(int[] arr, int L, int R) {
		if (L == R) {
			return 0;
		}
		return Math.min(f(arr, L + 1, R), f(arr, L, R - 1));
	}

	public static int windp(int[] arr) {
		// 如果输入数组为空或长度为0，返回0
		if (arr == null || arr.length == 0) {
			return 0;
		}

		int N = arr.length;

		// f[i][j] 表示从下标 i 到 j 范围内，先手玩家能获得的最大值
		int[][] f = new int[N][N];

		// s[i][j] 表示从下标 i 到 j 范围内，后手玩家能获得的最大值
		int[][] s = new int[N][N];

		// 初始化对角线元素，当只有一个元素时，先手玩家直接取走该元素
		//在这段代码中，
		// f[i][j] 表示从下标 i 到 j 范围内，先手玩家能获得的最大值；
		// s[i][j] 表示后手玩家能获得的最大值。
		// 当区间长度为1时（即只有一个元素），只有先手玩家可以取走这个元素，而后手玩家无法再取任何元素。
		// 因此，我们需要初始化这些情况：
		//这意味着：•当区间只包含一个元素时（即 i == j），先手玩家直接取走这个元素，因此 f[i][i] = arr[i]。
		// •后手玩家在这个情况下没有任何元素可以取，所以 s[i][i] 默认为0（虽然在这段代码中没有显式初始化，但可以通过后续的逻辑推导出来）。
		for (int i = 0; i < N; i++) {
			f[i][i] = arr[i];
		}

		// 填充动态规划表，从对角线开始向右下方移动
		// 例如：从 (0,1) 开始，逐步扩展到 (0,N-1)
		for (int col = 1; col < N; col++) {
			// 对角线出发位置 (0, col)，每次循环处理一个斜列
			int L = 0;
			int R = col;

			// 当 L 和 R 都在有效范围内时，继续处理
			while (L < N && R < N) {
				// 先手玩家可以选择 arr[L] 或 arr[R]
				// 选择 arr[L] 时，后手玩家面对的是 f[L+1][R] 的情况
				// 选择 arr[R] 时，后手玩家面对的是 f[L][R-1] 的情况
				f[L][R] = Math.max(arr[L] + s[L + 1][R], arr[R] + s[L][R - 1]);

				// 后手玩家在上述两种情况下能得到的最大值,因为玩家都很精明，所以后手玩家拿到的肯定没有先手玩家高
				// 选择 arr[L] 时，后手玩家面对的是 f[L+1][R] 的情况
				// 选择 arr[R] 时，后手玩家面对的是 f[L][R-1] 的情况
				s[L][R] = Math.min(f[L + 1][R], f[L][R - 1]);

				// 移动到下一个位置
				L++;
				R++;
			}
		}

		// 返回先手玩家在整个数组范围内的最大值
		return Math.max(f[0][N - 1], s[0][N - 1]);
	}

	public static void main(String[] args) {
		int[] arr = { 5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7 };
		//System.out.println(win1(arr));
		System.out.println(windp(arr));

	}

}
