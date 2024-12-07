package com.example.design.leetcode.二叉树.路径和;

import com.example.design.leetcode.二叉树.TreeNode;

/**
 * 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。如果存在，返回 true ；否则，返回 false 。
 * 叶子节点 是指没有子节点的节点。
 * 输入：root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
 * 输出：true
 * 解释：等于目标和的根节点到叶节点路径如上图所示。
 *
 * 输入：root = [1,2,3], targetSum = 5
 * 输出：false
 * 解释：树中存在两条根节点到叶子节点的路径：
 * (1 --> 2): 和为 3
 * (1 --> 3): 和为 4
 * 不存在 sum = 5 的根节点到叶子节点的路径。
 *
 * 输入：root = [], targetSum = 0
 * 输出：false
 * 解释：由于树是空的，所以不存在根节点到叶子节点的路径。
 */
public class Solution {
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if(root==null){
            return false;
        }
        int preSum = root.val;
        return pathSum(root,preSum,targetSum);
    }

    private static boolean pathSum(TreeNode root, int preSum, int targetSum) {
        if(root.left==null&&root.right==null){
            //叶子结点
            if(preSum==targetSum){
                return true;
            }else{
                return false;
            }

        }

        //非叶子节点
        if(root.left!=null){
            preSum+=root.left.val;
            if(pathSum(root.left,preSum,targetSum)==true){
                return true;
            }else{
                preSum-=root.left.val;
            }
        }
        if(root.right!=null){
            preSum+=root.right.val;
            if(pathSum(root.right,preSum,targetSum)==true){
                return true;
            }else{
                preSum-=root.right.val;
            }
        }
        return false;
    }



    public static void main(String[] args) {//root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
        TreeNode treeNode =new TreeNode(5);
        TreeNode treeNode2 =new TreeNode(4);
        TreeNode treeNode3 =new TreeNode(8);
        TreeNode treeNode4 =new TreeNode(11);
        TreeNode treeNode5 =new TreeNode(13);
        TreeNode treeNode6 =new TreeNode(4);
        TreeNode treeNode7 =new TreeNode(7);
        TreeNode treeNode8 =new TreeNode(2);
        TreeNode treeNode9 =new TreeNode(1);

        treeNode.left=treeNode2;
        treeNode.right=treeNode3;

        treeNode2.left=treeNode4;
        treeNode2.right=null;

        treeNode4.left=treeNode7;
        treeNode4.right=treeNode8;

        treeNode3.left=treeNode5;
        treeNode3.right=treeNode6;

        treeNode6.right=treeNode9;


        boolean b = hasPathSum(treeNode, 22);
        System.out.println(b);


    }

}
