package com.example.design.leetcode.二叉树.平衡二叉树;

import com.example.design.leetcode.二叉树.TreeNode;

/**
 * 给定一个二叉树，判断它是否是平衡二叉树
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：true
 *
 * 输入：root = [1,2,2,3,3,null,null,4,4]
 * 输出：false
 *
 */
public class Solution {

    /**
     * 我的思路是用深度搜索遍历左右节点高度后，判断相差高度
     * @param root
     * @return
     */
    public static boolean isBalanced(TreeNode root) {
        if(root == null){
            return true;
        }
        int leftDepth = deepSearch(root.left);
        int rightDepth = deepSearch(root.right);
        if(leftDepth==rightDepth||Math.abs(leftDepth-rightDepth)==1){
            return isBalanced(root.left)&&isBalanced(root.right);
        }else if(leftDepth>rightDepth+1){
            return false;
        }else if(1+leftDepth<rightDepth){
            return false;
        }else{
            return true;
        }


    }

    /**
     * 获取root节点的高度
     * @param root
     * @return
     */
    private static int deepSearch(TreeNode root) {
        if(root == null){
            return 0;
        }
        int leftDepth = deepSearch(root.left);
        int rightDepth = deepSearch(root.right);
        return Math.max(leftDepth,rightDepth)+1;

    }

    /**
     * 自下而上遍历每个节点的高度
     * @param root
     * @return
     */
    public boolean isBalanced2(TreeNode root) {
        return height(root) >= 0;
    }

    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        } else {
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }



    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(3,new TreeNode(9,null,null),new TreeNode(20,new TreeNode(15,null,null),new TreeNode(7,null,null)));
        TreeNode treeNode2 =new TreeNode(1,new TreeNode(2,new TreeNode(3,new TreeNode(4,null,null),new TreeNode(4,null,null)),new TreeNode(3,null,null)),new TreeNode(2,null,null));
        TreeNode treeNode3 =new TreeNode(1,null,null);
        TreeNode treeNode4 =new TreeNode(1,new TreeNode(2,new TreeNode(3,new TreeNode(4,null,null),null),null),new TreeNode(2,null,new TreeNode(3,null,new TreeNode(4,null,null))));
        boolean balanced = isBalanced(treeNode4);
        System.out.println(balanced);
    }
}
