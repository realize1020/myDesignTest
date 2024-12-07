package com.example.design.leetcode.二叉树.路径和2;

import com.example.design.leetcode.二叉树.TreeBuilder;
import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
 * 叶子节点 是指没有子节点的节点
 *
 * 输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * 输出：[[5,4,11,2],[5,8,4,5]]
 *
 * 输入：root = [1,2,3], targetSum = 5
 * 输出：[]
 *
 * 输入：root = [1,2], targetSum = 0
 * 输出：[]
 *
 *
 * 和1不一样的是，需要返回所有的路径和结点。
 * 所以意味着原本的递归是根据布尔值找到就返回true就不适用了，因为找到一个之后还需要回退然后继续找
 */
public class Solution {
    public static List<List<Integer>> ans =null;
    public static List<Integer> collect =null;
    public static int preSum=0;

    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        if(root==null){
            return ans;
        }
        ans=new ArrayList<>();
        collect =new ArrayList<>();
        preSum=root.val;
        collect.add(root.val);
        pathSum(root,preSum,targetSum,ans,collect);
        return ans;
    }

    /**
     * 自己根据路径和1来改写！成功！需要优化
     * @param root
     * @param preSum
     * @param targetSum
     * @param ans
     * @param collect
     * @return
     */
    private static boolean pathSum(TreeNode root, int preSum, int targetSum,List<List<Integer>> ans,List<Integer> collect) {

        if(root.left==null&&root.right==null){
            //叶子结点
            if(preSum==targetSum){
                List<Integer> collect2 =new ArrayList(collect);//不能直接直接添加collect，因为后面删除后，ans里也会被删除，所以使用一个collect2
                ans.add(collect2);
                return true;
            }else{
                return false;
            }

        }

        //非叶子节点
        if(root.left!=null){
            collect.add(root.left.val);
            preSum+=root.left.val;
            if(pathSum(root.left,preSum,targetSum,ans,collect)==true){
                collect.remove(collect.size()-1);//这个地方collect.remove(root.left.val);感觉有点问题，remove方法删除数据，万一有两个一样的节点数据怎么办，是不是改成remove(index)
                preSum-=root.left.val;
                //把叶节点置为-1
                root.left.val=-1;
            }else{
                collect.remove(collect.size()-1);//这个地方collect.remove(root.left.val);感觉有点问题，remove方法删除数据，万一有两个一样的节点数据怎么办，是不是改成remove(index)
                preSum-=root.left.val;
            }
        }
        if(root.right!=null){
            collect.add(root.right.val);
            preSum+=root.right.val;
            if(pathSum(root.right,preSum,targetSum,ans,collect)==true){
                collect.remove(collect.size()-1);
                preSum-=root.right.val;
                //把叶节点置为-1
                root.right.val=-1;
            }else{
                collect.remove(collect.size()-1);
                preSum-=root.right.val;
            }
        }
        return (root.left!=null&&root.left.val==-1)||(root.right!=null&&root.right.val==-1)?true:false;
    }


    /**
     * 左程云
     * @param root
     * @param sum
     * @return
     */
    public static List<List<Integer>> pathSum2(TreeNode root, int sum) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        ArrayList<Integer> path = new ArrayList<>();
        process(root, path, 0, sum, ans);
        return ans;
    }

    public static void process(TreeNode x, List<Integer> path, int preSum, int sum, List<List<Integer>> ans) {
        if (x.left == null && x.right == null) {
            if (preSum + x.val == sum) {
                path.add(x.val);
                ans.add(copy(path));//不能直接直接添加，因为后面删除后，ans里也会被删除
                path.remove(path.size() - 1);
            }
            return;
        }
        // x 非叶节点
        path.add(x.val);
        preSum += x.val;
        if (x.left != null) {
            process(x.left, path, preSum, sum, ans);
        }
        if (x.right != null) {
            process(x.right, path, preSum, sum, ans);
        }
        path.remove(path.size() - 1);
    }

    public static List<Integer> copy(List<Integer> path) {
        List<Integer> ans = new ArrayList<>();
        for (Integer num : path) {
            ans.add(num);
        }
        return ans;
    }

    public static void main(String[] args) {
        Integer[] arr = {5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1};
        //Integer[] arr = {1,2,3};
        TreeNode root = TreeBuilder.buildTree(arr);

        List<List<Integer>> lists = pathSum2(root, 22);
        System.out.println(lists);
    }

}
