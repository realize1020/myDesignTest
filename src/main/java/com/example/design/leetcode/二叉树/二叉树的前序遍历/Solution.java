package com.example.design.leetcode.二叉树.二叉树的前序遍历;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
 * 输入：root = [1,null,2,3]
 * 输出：[1,2,3]
 * 示例 2：
 *
 * 输入：root = []
 * 输出：[]
 * 示例 3：
 *
 * 输入：root = [1]
 * 输出：[1]
 */
public class Solution {
    /**
     * 递归方式
     * @param root
     * @return
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        if(root ==null){
            return new ArrayList<Integer>();
        }
        List<Integer> treeList =new ArrayList<>();
        //先输出根节点
        Integer value = root.val;
        treeList.add(value);
        //然后遍历左子树
        List<Integer> listLeft = preorderTraversal(root.left);
        treeList.addAll(listLeft);

        //再遍历右子树
        List<Integer> listRight = preorderTraversal(root.right);
        treeList.addAll(listRight);


        return treeList;
    }

    /**
     * 迭代方式
     *
     */
    public static List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        if (root == null) {
            return result;
        }

        stack.push(root); // 初始将根节点入栈

        while (!stack.isEmpty()) {
            TreeNode currentNode = stack.pop(); // 出栈并访问当前节点
            result.add(currentNode.val);

            // 先压右子节点（保证左子树先遍历），再压左子节点
            if (currentNode.right != null) {
                stack.push(currentNode.right);
            }
            if (currentNode.left != null) {
                stack.push(currentNode.left);
            }
        }

        return result;
    }



    public static List<Integer> preorderTraversal3(TreeNode root){
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        if(root!=null){
            stack.push(root);
        }
        while(!stack.isEmpty()){
            TreeNode node = stack.peek();
            if(node!=null){
                stack.pop();// 将该节点弹出，避免重复操作，下面再将右中左节点添加到栈中
                if(node.right!=null) stack.push(node.right);//右

                if(node.left!=null) stack.push(node.left);//左

                stack.push(node);//中
                stack.push(null);// 中节点访问过，但是还没有处理，加入空节点做为标记。


            }else{// 只有遇到空节点的时候，才将下一个节点放进结果集
                stack.pop();
                node = stack.pop();
                //stack.pop();
                result.add(node.val);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        TreeNode treeNode1 =new TreeNode(1,null,new TreeNode(2,new TreeNode(3),null));

        //List<Integer> list =preorderTraversal(treeNode1);
        List<Integer> list =preorderTraversal3(treeNode1);
        System.out.println(list);
    }
}
