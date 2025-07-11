package com.example.design.leetcode.二叉树.AC自动机.返回匹配成功的数量;

import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    public static class Node {
        public int end; // 有多少个字符串以该节点结尾
        public Node fail;
        public Node[] nexts;

        public Node() {
            end = 0;//标记有多少个匹配串以该节点结尾（若为 0 表示非结尾）
            fail = null;//失败指针，指向当前节点的最长后缀匹配节点
            nexts = new Node[26];
        }
    }

    public static class ACAutomation {
        private Node root;

        public ACAutomation() {
            root = new Node();
        }

        // 你有多少个匹配串，就调用多少次insert
        public void insert(String s) {
            char[] str = s.toCharArray();
            Node cur = root;
            int index = 0;
            for (int i = 0; i < str.length; i++) {
                index = str[i] - 'a';
                if (cur.nexts[index] == null) {//若字符对应路径不存在，则创建新节点。
                    Node next = new Node();
                    cur.nexts[index] = next;
                }
                cur = cur.nexts[index];//从根节点开始，遍历字符串的每个字符
            }
            cur.end++;//遍历结束后，将最后节点的 end 加 1，表示有一个匹配串在此结束。
        }

        /**
         * 构建失败指针（fail 指针
         * 核心思想：BFS + KMP 思想。
         */
        public void build() {
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            Node cur = null;
            Node cfail = null;
            while (!queue.isEmpty()) {
                cur = queue.poll(); // 父
                for (int i = 0; i < 26; i++) { // 下级所有的路
                    if (cur.nexts[i] != null) { // 该路下有子节点
                        cur.nexts[i].fail = root; // 初始时先设置一个值，将根节点的所有子节点的 fail 指针初始化为根节点。
                        cfail = cur.fail;
                        while (cfail != null) { // cur不是头节点
                            if (cfail.nexts[i] != null) {//对每个节点，从其父节点的 fail 指针开始向上查找，直到找到某个节点的子节点包含当前字符，将当前节点的 fail 指针指向该子节点。若找不到，则 fail 指针指向根节点。
                                cur.nexts[i].fail = cfail.nexts[i];
                                break;
                            }
                            cfail = cfail.fail;
                        }
                        queue.add(cur.nexts[i]);
                    }
                }
            }
        }

        /**
         * 统计文本中包含的所有匹配串的数量（每个匹配串只计数一次）
         * fail指针的指向规则：
         * 前缀树的头结点的 fail 指针一定指向空；
         * 头结点的孩子节点的 fail 指针一律指向头结点；
         * 其他节点 X 的 fail 指针的设置：
         * 记录该节点 X 到父节点的路径 path
         * 判断父节点的 fail 指针指向的节点node是否存在同样的 path
         * 如果不存在，则继续往上，查看 node 的 fail 指针指向的节点，一直往上，如果遇到空指针，则 X 的 fail 指针指向 头结点；
         * 若在一直往上的路途中某个节点存在路径 path，则X的 fail 指针指向该节点通过路径 path 到达的节点。
         * @param content
         * @return
         */
        public int containNum(String content) {
            char[] str = content.toCharArray();
            Node cur = root;
            Node follow = null;
            int index = 0;
            int ans = 0;
            for (int i = 0; i < str.length; i++) {
                index = str[i] - 'a';
                while (cur.nexts[index] == null && cur != root) {
                    cur = cur.fail;
                }
                cur = cur.nexts[index] != null ? cur.nexts[index] : root;
                follow = cur;
                while (follow != root) {
                    if (follow.end == -1) {//若节点的 end > 0，说明找到一个匹配串，计数并将 end 设为 -1（避免重复计数）
                        break;
                    }
                    { // 不同的需求，在这一段{ }之间修改
                        ans += follow.end;
                        follow.end = -1;
                    } // 不同的需求，在这一段{ }之间修改
                    follow = follow.fail;//继续沿 fail 指针向上，直到根节点。
                }
            }
            return ans;
        }

    }

    public static void main(String[] args) {
        ACAutomation ac = new ACAutomation();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("c");
        ac.build();
        System.out.println(ac.containNum("cdhe"));
    }
}
