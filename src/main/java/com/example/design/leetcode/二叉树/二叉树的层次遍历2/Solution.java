package com.example.design.leetcode.二叉树.二叉树的层次遍历2;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 给你二叉树的根节点 root ，返回其节点值 自底向上的层序遍历 。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[15,7],[9,20],[3]]
 * 示例 2：
 *
 * 输入：root = [1]
 * 输出：[[1]]
 * 示例 3：
 *
 * 输入：root = []
 * 输出：[]
 */
public class Solution {
    public static void main(String[] args) {
        //TreeNode root = new TreeNode(3, new TreeNode(9, null,null), new TreeNode(20,new TreeNode(15,null,null),new TreeNode(7,null,null)));
        TreeNode root = new TreeNode(1,null,null);
        List<List<Integer>> lists = levelOrderBottom(root);
        lists.stream().forEach(System.out::println);
    }

    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> ans =new ArrayList<>();
        if(root==null){
            return ans;
        }
        Queue<TreeNode> queue =new LinkedList<TreeNode>();
        queue.offer(root);

        while(!queue.isEmpty()){
            int size = queue.size();//size一直等于2，每层两个
            ArrayList<Integer> levelAns =new ArrayList<>();
            for(int i=0;i<size;i++){//每一层的循环控制
                TreeNode node = queue.poll();
                levelAns.add(node.val);
                if(node.left!=null){
                    queue.offer(node.left);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                }
            }
            ans.add(0,levelAns);
        }
        return ans;
    }
}
