package com.example.design.leetcode.二叉树.二叉树的中序遍历;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 输入：root = [1,null,2,3]
 * 输出：[1,3,2]
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
    public static  List<Integer> inorderTraversal(TreeNode root) {
        if(root ==null){
            return new ArrayList<Integer>();
        }
        List<Integer> treeList =new ArrayList<>();
        //遍历左子树
        List<Integer> listLeft = inorderTraversal(root.left);
        treeList.addAll(listLeft);
        //输入根节点
        Integer value = root.val;
        treeList.add(value);


        //遍历右子树
        List<Integer> listRight = inorderTraversal(root.right);
        treeList.addAll(listRight);


        return treeList;
    }

    /**
     * 迭代方式
     *
     */

    public static  List<Integer> inorderTraversal2(TreeNode root){
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        TreeNode originalRoot = root;
        TreeNode cur = root;

        while(cur!=null||!stack.isEmpty()){
            if(cur!=null){
                stack.push(cur);
                cur = cur.left;
            }else{
                cur=stack.pop();
                //stack.pop();
                result.add(cur.val);
                cur=cur.right;
            }

        }
        return result;

    }

    /**
     * 统一写法
     * @param root
     * @return
     */
    public static  List<Integer> inorderTraversal3(TreeNode root){
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        if(root!=null){
            stack.push(root);
        }
        while(!stack.isEmpty()){
            TreeNode node = stack.peek();
            if(node!=null){
                stack.pop();// 将该节点弹出，避免重复操作，下面再将右中左节点添加到栈中
                if(node.right!=null) stack.push(node.right);

                stack.push(node);
                stack.push(null);// 中节点访问过，但是还没有处理，加入空节点做为标记。

                if(node.left!=null) stack.push(node.left);

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
        //TreeNode treeNode1 =new TreeNode(1,null,new TreeNode(2,new TreeNode(3),null));
        TreeNode treeNode1 =new TreeNode(1,new TreeNode(2),new TreeNode(3));


        List<Integer> list =inorderTraversal3(treeNode1);
        System.out.println(list);

    }
}
