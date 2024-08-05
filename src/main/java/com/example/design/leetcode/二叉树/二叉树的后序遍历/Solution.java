package com.example.design.leetcode.二叉树.二叉树的后序遍历;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 示例 1：
 *
 *
 * 输入：root = [1,null,2,3]
 * 输出：[3,2,1]
 */
public class Solution {
    public static List<Integer> postorderTraversal(TreeNode root) {
        if(root ==null){
            return new ArrayList<Integer>();
        }
        List<Integer> treeList =new ArrayList<>();

        //先遍历左子树
        List<Integer> listLeft = postorderTraversal(root.left);
        treeList.addAll(listLeft);

        //再遍历右子树
        List<Integer> listRight = postorderTraversal(root.right);
        treeList.addAll(listRight);

        //最后输出根节点
        Integer value = root.val;
        treeList.add(value);


        return treeList;
    }

    /**
     * 迭代法
     * @param root
     * @return
     */
    public static List<Integer> postorderTraversal2(TreeNode root){
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        if (root == null) {
            return result;
        }

        stack.push(root); // 初始将根节点入栈

        while(!stack.isEmpty()){
            TreeNode currentNode = stack.pop();
            result.add(currentNode.val);
            if(currentNode.left!=null){
                stack.push(currentNode.left);
            }
            if(currentNode.right!=null){
                stack.push(currentNode.right);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 迭代法统一写法
     * @param root
     * @return
     */
    public static List<Integer> postorderTraversal3(TreeNode root){

        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        if(root!=null){
            stack.push(root);
        }
        while(!stack.isEmpty()){
            TreeNode node = stack.peek();
            if(node!=null){
                stack.pop();// 将该节点弹出，避免重复操作，下面再将右中左节点添加到栈中

                stack.push(node);//中
                stack.push(null);// 中节点访问过，但是还没有处理，加入空节点做为标记。

                if(node.right!=null) stack.push(node.right);//右

                if(node.left!=null) stack.push(node.left);//左


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

        List<Integer> list =postorderTraversal3(treeNode1);
        System.out.println(list);
    }
}
