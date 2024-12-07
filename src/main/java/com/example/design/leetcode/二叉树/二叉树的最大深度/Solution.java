package com.example.design.leetcode.二叉树.二叉树的最大深度;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * 给定一个二叉树 root ，返回其最大深度。
 *
 * 二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：3
 * 输入：root = [1,null,2]
 * 输出：2
 */
public class Solution {

    /**
     * 这个公式法得是完全二叉树才可以
     * @param root
     * @return
     */
    public static int maxDepth(TreeNode root) {
        if(root==null){
            return 0;
        }
        //先遍历，得到遍历序列，然后知道有多少节点
        List<Integer> list = com.example.design.leetcode.二叉树.二叉树的前序遍历.Solution.preorderTraversal(root);
        double result = Math.log(list.size())/Math.log(2);
        return (int)result+1;


    }

    /**
     * 深度优先搜索
     * @param root
     * @return
     */
    public static int maxDepth2(TreeNode root){
        if(root==null){
            return 0;
        }

        int l = maxDepth2(root.left);//搜索左子树
        int r = maxDepth2(root.right);//搜索右子树

        return Math.max(l,r)+1;

    }

    /**
     * 广度优先搜索(只能用队列，先进先出，不能用栈，因为需要在每一层的左右节点遍历结束时再层数+1，不然的话栈结构会先弹出下一层的数据，最后回溯的时候，会错乱
     *
     * @param root
     * @return
     */
    public static int maxDepth3(TreeNode root){
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        int ans = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                size--;
            }
            ans++;
        }
        return ans;

    }

    /**
     * 不能用这个栈结构模仿上面的队列实现方式，有错误
     * @param root
     * @return
     */

    public static int maxDepth4(TreeNode root){
        int ant=0;
        Stack<TreeNode> stack = new Stack<>();

        if (root == null) {
            return ant;
        }

        stack.push(root); // 初始将根节点入栈

        while (!stack.isEmpty()) {
            int size = stack.size();
            while(size>0){
                TreeNode currentNode = stack.pop(); // 出栈并访问当前节点
                // 先压右子节点（保证左子树先遍历），再压左子节点
                if (currentNode.right != null) {
                    stack.push(currentNode.right);
                }
                if (currentNode.left != null) {
                    stack.push(currentNode.left);
                }
                size--;
            }

            ant++;
        }

        return ant;

    }

    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(3,new TreeNode(9,null,null),new TreeNode(20,new TreeNode(15,null,null),new TreeNode(7,null,null)));
        TreeNode treeNode2 =new TreeNode(1,new TreeNode(2,new TreeNode(4,null,null),null),new TreeNode(3,null,new TreeNode(5,null,null)));
        int result = maxDepth3(treeNode2);
        System.out.println(result);
    }
}
