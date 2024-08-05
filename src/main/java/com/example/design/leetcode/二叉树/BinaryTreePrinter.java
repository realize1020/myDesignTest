package com.example.design.leetcode.二叉树;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTreePrinter {
    // 前序遍历
    public void printPreorder(TreeNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");
        printPreorder(root.left);
        printPreorder(root.right);
    }

    // 中序遍历
    public void printInorder(TreeNode root) {
        if (root == null) return;
        printInorder(root.left);
        System.out.print(root.val + " ");
        printInorder(root.right);
    }

    // 后序遍历
    public void printPostorder(TreeNode root) {
        if (root == null) return;
        printPostorder(root.left);
        printPostorder(root.right);
        System.out.print(root.val + " ");
    }

    // 层次遍历
    public void printLevelOrder(TreeNode root) {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.val + " ");
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
    }
}
