package com.example.design.leetcode.二叉树.翻转二叉树;

import com.example.design.leetcode.二叉树.BinaryTreePrinter;
import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
 * 输入：root = [4,2,7,1,3,6,9]
 * 输出：[4,7,2,9,6,3,1]
 *
 * 输入：root = [2,1,3]
 * 输出：[2,3,1]
 */
public class Solution {
    /**
     * 使用广度优先搜索(层次遍历)来反转二叉树
     * @param root
     * @return
     */
    public static TreeNode invertTree(TreeNode root) {
        if(root == null){
            return null;
        }
        Queue<TreeNode> queue =new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()){
            int size = queue.size();

            while(size>0){
                TreeNode treeNode = queue.poll();
                if(treeNode.left!=null){
                    queue.offer(treeNode.left);
                }
                if(treeNode.right!=null){
                    queue.offer(treeNode.right);
                }
                if(treeNode.left!=null||treeNode.right!=null){
                    TreeNode temp=treeNode.left;
                    treeNode.left=treeNode.right;
                    treeNode.right = temp;
                }

                size--;
            }
        }
        return root;

    }

    /**
     * leetcode官方使用层次遍历
     * @param root
     * @return
     */
    public static TreeNode invertTree0(TreeNode root){
        if (root == null) {
            return null;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return root;
    }

    /**
     * 使用深度优先搜索来遍历并反转二叉树，先递归到叶子树再反转
     * @param root
     * @return
     */
    public static TreeNode invertTree2(TreeNode root){
        if(root == null){
            return null;
        }

        TreeNode leftNode = invertTree2(root.left);
        TreeNode rightNode =invertTree2(root.right);
//        if(leftNode!=null&&rightNode!=null){
//            TreeNode temp=root.left;
//            root.left=root.right;
//            root.right = temp;
//        }
        //不加null判断，反转后的序列里带null。加了判断可以把null过滤掉
        TreeNode temp=root.left;
        root.left=root.right;
        root.right = temp;

        return root;
    }

    /**
     * 同上、先反转再递归也可以
     * @param root
     * @return
     */
    public static TreeNode invertTree3(TreeNode root){
        if(root == null){
            return null;
        }
        TreeNode temp=root.left;
        root.left=root.right;
        root.right = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /**
     * leetcode官方递归反转
     * @param root
     * @return
     */
    public static TreeNode invertTree4(TreeNode root){
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }


    /**
     * 深度优先遍历的方式反转
     */
    private static TreeNode invertTreeByStack(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = stack.pop();
                TreeNode temp = cur.left;
                cur.left = cur.right;
                cur.right = temp;
                if (cur.right != null) {
                    stack.push(cur.right);
                }
                if (cur.left != null) {
                    stack.push(cur.left);
                }
            }
        }
        return root;
    }


    public static void main(String[] args) {
        BinaryTreePrinter binaryTreePrinter =new BinaryTreePrinter();
        TreeNode treeNode =new TreeNode(2,new TreeNode(1,null,null),new TreeNode(3,null,null));
        TreeNode treeNode2 =new TreeNode(4,new TreeNode(2,new TreeNode(1,null,null),new TreeNode(3,null,null)),new TreeNode(7,new TreeNode(6,null,null),new TreeNode(9,null,null)));
        TreeNode treeNode3 =new TreeNode(1,null,new TreeNode(2,null,null));
        System.out.println("翻转前：");
        binaryTreePrinter.printLevelOrder(treeNode2);
        TreeNode root = invertTreeByStack(treeNode2);

        System.out.println();
        System.out.println("翻转后：");
        binaryTreePrinter.printLevelOrder(root);
    }
}
