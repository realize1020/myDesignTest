package com.example.design.leetcode.从暴力递归到动态规划;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 我们有 n 种不同的贴纸。每个贴纸上都有一个小写的英文单词。
 *
 * 您想要拼写出给定的字符串 target ，方法是从收集的贴纸中切割单个字母并重新排列它们。如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
 *
 * 返回你需要拼出 target 的最小贴纸数量。如果任务不可能，则返回 -1 。
 *
 * 注意：在所有的测试用例中，所有的单词都是从 1000 个最常见的美国英语单词中随机选择的，并且 target 被选择为两个随机单词的连接。
 *
 * 示例 1：
 *
 * 输入： stickers = ["with","example","science"], target = "thehat"
 * 输出：3
 * 解释：
 * 我们可以使用 2 个 "with" 贴纸，和 1 个 "example" 贴纸。
 * 把贴纸上的字母剪下来并重新排列后，就可以形成目标 “thehat“ 了。
 * 此外，这是形成目标字符串所需的最小贴纸数量。
 *
 * 示例 2:
 *
 * 输入：stickers = ["notice","possible"], target = "basicbasic"
 * 输出：-1
 * 解释：我们不能通过剪切给定贴纸的字母来形成目标“basicbasic”。
 */
public class StickersToSpellWord {

	public static int minStickers1(String[] stickers, String target) {
		
		int n = stickers.length;
		
		int[][] map = new int[n][26];// stickers -> [26] [26] [26],用一个map代替stickers，把stickers中的字母和数量转化为map
		for (int i = 0; i < n; i++) {
			char[] str = stickers[i].toCharArray();
			for (char c : str) {
				map[i][c - 'a']++;
			}
		}
		HashMap<String, Integer> dp = new HashMap<>();
		dp.put("", 0);
		return process1(dp, map, target);
	}

	// dp 傻缓存，如果t已经算过了，直接返回dp中的值
	// t 剩余的目标
	// 0..N每一个字符串所含字符的词频统计
	// 返回值是-1，map 中的贴纸  怎么都无法rest
	public static int process1(HashMap<String, Integer> dp, int[][] map, String rest) {
		if (dp.containsKey(rest)) {
			return dp.get(rest);
		}
		// 以下就是正式的递归调用过程
		int ans = Integer.MAX_VALUE; // ans -> 搞定rest，使用的最少的贴纸数量 
		int n = map.length; // N种贴纸
		int[] tmap = new int[26]; // tmap 去替代 rest,因为不需要考虑rest中字母顺序，只是数量问题
		char[] target = rest.toCharArray();
		for (char c : target) {
			tmap[c - 'a']++;
		}
		for (int i = 0; i < n; i++) {
			// 枚举当前第一张贴纸是谁？
			if (map[i][target[0] - 'a'] == 0) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			// i 贴纸， j 枚举a~z字符
			for (int j = 0; j < 26; j++) { // 
				if (tmap[j] > 0) { // j这个字符是target需要的
					for (int k = 0; k < Math.max(0, tmap[j] - map[i][j]); k++) {
						sb.append((char) ('a' + j));
					}
				}
			}
			// sb ->  i
			String s = sb.toString();
			int tmp = process1(dp, map, s);
			if (tmp != -1) {
				ans = Math.min(ans, 1 + tmp);
			}
		}
		// ans 系统最大  rest
		dp.put(rest, ans == Integer.MAX_VALUE ? -1 : ans);
		return dp.get(rest);
	}

	public static int minStickers2(String[] stickers, String target) {
		int n = stickers.length;
		int[][] map = new int[n][26];
		for (int i = 0; i < n; i++) {
			char[] str = stickers[i].toCharArray();
			for (char c : str) {
				map[i][c - 'a']++;
			}
		}
		char[] str = target.toCharArray();
		int[] tmap = new int[26];
		for (char c : str) {
			tmap[c - 'a']++;
		}
		HashMap<String, Integer> dp = new HashMap<>();
		int ans = process2(map, 0, tmap, dp);
		return ans;
	}

	public static int process2(int[][] map, int i, int[] tmap, HashMap<String, Integer> dp) {
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(i + "_");
		for (int asc = 0; asc < 26; asc++) {
			if (tmap[asc] != 0) {
				keyBuilder.append((char) (asc + 'a') + "_" + tmap[asc] + "_");
			}
		}
		String key = keyBuilder.toString();
		if (dp.containsKey(key)) {
			return dp.get(key);
		}
		boolean finish = true;
		for (int asc = 0; asc < 26; asc++) {
			if (tmap[asc] != 0) {
				finish = false;
				break;
			}
		}
		if (finish) {
			dp.put(key, 0);
			return 0;
		}
		if (i == map.length) {
			dp.put(key, -1);
			return -1;
		}
		int maxZhang = 0;
		for (int asc = 0; asc < 26; asc++) {
			if (map[i][asc] != 0 && tmap[asc] != 0) {
				maxZhang = Math.max(maxZhang, (tmap[asc] / map[i][asc]) + (tmap[asc] % map[i][asc] == 0 ? 0 : 1));
			}
		}
		int[] backup = Arrays.copyOf(tmap, tmap.length);
		int min = Integer.MAX_VALUE;
		int next = process2(map, i + 1, tmap, dp);
		tmap = Arrays.copyOf(backup, backup.length);
		if (next != -1) {
			min = next;
		}
		for (int zhang = 1; zhang <= maxZhang; zhang++) {
			for (int asc = 0; asc < 26; asc++) {
				tmap[asc] = Math.max(0, tmap[asc] - (map[i][asc] * zhang));
			}
			next = process2(map, i + 1, tmap, dp);
			tmap = Arrays.copyOf(backup, backup.length);
			if (next != -1) {
				min = Math.min(min, zhang + next);
			}
		}
		int ans = min == Integer.MAX_VALUE ? -1 : min;
		dp.put(key, ans);
		return ans;
	}
	
	public static void main(String[] args) {
		String[] arr = {"aaaa","bbaa","ccddd"};
		String str = "abcccdddbbbaaa";
		System.out.println(minStickers1(arr, str));
		//System.out.println(minStickers2(arr, str));
		
		
	}

}
