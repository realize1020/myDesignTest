package com.example.design.leetcode.二叉树.前序和中序序列创建二叉树;

import cn.hutool.core.util.ArrayUtil;
import com.example.design.leetcode.二叉树.BinaryTreePanel;
import com.example.design.leetcode.二叉树.BinaryTreePrinter;
import com.example.design.leetcode.二叉树.TreeNode;

import javax.swing.*;
import java.util.Arrays;

public class Solution {
    public static void main(String[] args) {
        int[] pre = {1,2,4,7,3,5,6,8};//前序序列

        int[] ins = {4,7,2,1,5,3,8,6};//中序序列

        TreeNode treeNode = createTreeNode(pre, ins);

        BinaryTreePrinter printer = new BinaryTreePrinter();
        System.out.println("前序遍历:");
        printer.printPreorder(treeNode);
        System.out.println("\n中序遍历:");
        printer.printInorder(treeNode);
        System.out.println("\n后序遍历:");
        printer.printPostorder(treeNode);
        System.out.println("\n层次遍历:");
        printer.printLevelOrder(treeNode);


        JFrame frame = new JFrame("二叉树展示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // 示例二叉树

        BinaryTreePanel panel = new BinaryTreePanel(treeNode);
        frame.getContentPane().add(panel);

        frame.setVisible(true);


    }

    public static TreeNode createTreeNode(int[] pre,int[] ins){
        if(ArrayUtil.isEmpty(pre)||ArrayUtil.isEmpty(ins)){
            return null;
        }
        if(pre.length!=ins.length){
            return null;
        }
        int val = pre[0];
        TreeNode treeNode=new TreeNode(val);
//        for(int i=0;i<pre.length;i++){
//            if(pre[0]==ins[i]){
//                int[] pre2 = Arrays.copyOfRange(pre, 1, i+1);
//                int[] ins2 = Arrays.copyOfRange(ins, 0, i);
//                treeNode.left=createTreeNode(pre2,ins2);
//                int[] pre3 = Arrays.copyOfRange(pre, i+1, pre.length);
//                int[] ins4 = Arrays.copyOfRange(ins, i+1, ins.length);
//                treeNode.right=createTreeNode(pre3,ins4);
//            }
//        }

        //下面也可以。
        int[] pre2 = Arrays.copyOfRange(pre, 1, findIndex(ins, val)+1);
        int[] ins2 = Arrays.copyOfRange(ins, 0, findIndex(ins, val));
        treeNode.left=createTreeNode(pre2,ins2);
        int[] pre3 = Arrays.copyOfRange(pre, findIndex(ins, val)+1, pre.length);
        int[] ins4 = Arrays.copyOfRange(ins, findIndex(ins, val)+1, ins.length);
        treeNode.right=createTreeNode(pre3,ins4);

        return treeNode;
    }


    public static int findIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // 找到了，返回索引
            }
        }
        return -1; // 没找到，返回-1表示未找到
    }
}
