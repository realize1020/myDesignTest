package com.example.design.leetcode.二叉树.检查二叉树是否是满二叉树;

import com.example.design.leetcode.二叉树.TreeNode;

public class Solution {
    public static void main(String[] args) {
        TreeNode node =new TreeNode(0);
        TreeNode node2= new TreeNode(1);
        TreeNode node3= new TreeNode(2);
        node.left=node2;
        node.right=node3;
//        TreeNode node4= new TreeNode(3);
//        TreeNode node5= new TreeNode(4);
//        node2.left=node4;
//        node2.right=node5;
        boolean full1 = isFull1(node);
        System.out.println(full1);
    }

    /**
     * 是否是满二叉树
     * @param head
     * @return
     */
    public static boolean isFull1(TreeNode head) {
        if (head == null) {
            return true;
        }
        int height = h(head);
        int TreeNodes = n(head);
        return (1 << height) - 1 == TreeNodes;
    }

    /**
     * 树的高度
     * @param head
     * @return
     */
    public static int h(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return Math.max(h(head.left), h(head.right)) + 1;
    }

    /**
     * 结点个数
     * @param head
     * @return
     */
    public static int n(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return n(head.left) + n(head.right) + 1;
    }

    public static boolean isFull2(TreeNode head) {
        if (head == null) {
            return true;
        }
        Info all = process(head);
        return (1 << all.height) - 1 == all.nodes;
    }

    public static class Info {
        public int height;
        public int nodes;

        public Info(int h, int n) {
            height = h;
            nodes = n;
        }
    }

    public static Info process(TreeNode head) {
        if (head == null) {
            return new Info(0, 0);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;
        return new Info(height, nodes);
    }
}
