package com.example.design.leetcode.二叉树.判断二叉树是否为二叉搜索树;

import com.example.design.leetcode.二叉树.BinaryTreePanel;
import com.example.design.leetcode.二叉树.TreeNode;

import javax.swing.*;

public class Solution {
    public static class Info {
        public boolean isBST;
        public int max;
        public int min;

        public Info(boolean isBST, int max, int min) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
        }
    }

    /**
     * 左成云
     * @param x
     * @return
     */
    public static Info process(TreeNode x) {
        if (x == null) {
            // 空节点被认为是 BST，最大值和最小值可以设置为任意值，这里使用 Integer.MIN_VALUE 和 Integer.MAX_VALUE。
            return new Info(true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        // 递归处理左右子树
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);

        // 计算当前节点的最大值和最小值
        int max = Math.max(x.val, Math.max(leftInfo.max, rightInfo.max));
        int min = Math.min(x.val, Math.min(leftInfo.min, rightInfo.min));

        // 判断当前子树是否为 BST
        boolean isBST = true;

        // 如果左子树不是 BST，则当前子树也不是
        if (!leftInfo.isBST) {
            isBST = false;
        }

        // 如果右子树不是 BST，则当前子树也不是
        if (!rightInfo.isBST) {
            isBST = false;
        }

        // 检查当前节点是否满足 BST 的性质：左子树的最大值小于当前节点值，右子树的最小值大于当前节点值
        boolean leftMaxLessX = leftInfo.max < x.val;
        boolean rightMinMoreX = rightInfo.min > x.val;

        if (!(leftMaxLessX && rightMinMoreX)) {
            isBST = false;
        }

        // 返回当前子树的信息
        return new Info(isBST, max, min);
    }

    /**
     * 左
     * @param x
     * @return
     */
    	public static Info process2(TreeNode x) {
		if (x == null) {
			return null;
		}
		Info leftInfo = process2(x.left);
		Info rightInfo = process2(x.right);
		int max = x.val;
		int min = x.val;
		if (leftInfo != null) {
			max = Math.max(leftInfo.max, max);
			min = Math.min(leftInfo.min, min);
		}
		if (rightInfo != null) {
			max = Math.max(rightInfo.max, max);
			min = Math.min(rightInfo.min, min);
		}
		boolean isBST = true;
		if (leftInfo != null && !leftInfo.isBST) {
			isBST = false;
		}
		if (rightInfo != null && !rightInfo.isBST) {
			isBST = false;
		}
		boolean leftMaxLessX = leftInfo == null ? true : (leftInfo.max < x.val);
		boolean rightMinMoreX = rightInfo == null ? true : (rightInfo.min > x.val);
		if (!(leftMaxLessX && rightMinMoreX)) {
			isBST = false;
		}
		return new Info(isBST, max, min);
	}

    public static Info process3(TreeNode root){
    	if(root==null){
    	    return null;
        }
        Info leftInfo = process3(root.left);
        Info rightInfo = process3(root.right);
        boolean isBST=true;
        if((leftInfo!=null&&!leftInfo.isBST)||(rightInfo!=null&&!rightInfo.isBST)){
            isBST=false;
        }
        int max=root.val;
        int min=root.val;
        if(leftInfo!=null){
            if(max<leftInfo.max){
                isBST=false;
            }
            max = Math.max(max,leftInfo.max);
            min=Math.min(min,leftInfo.min);

        }
        if(rightInfo!=null){
            if(min>rightInfo.min){
                isBST=false;
            }
            min=Math.min(min,rightInfo.min);
            max=Math.max(max,rightInfo.max);
        }

        return new Info(isBST,max,min);



    }


    public static void main(String[] args) {
        // 测试代码
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(4);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);

        JFrame frame = new JFrame("二叉树展示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        BinaryTreePanel panel = new BinaryTreePanel(root);
        frame.getContentPane().add(panel);

        frame.setVisible(true);



        Info result = process3(root);
        System.out.println("Is BST: " + result.isBST);
        System.out.println("Max: " + result.max);
        System.out.println("Min: " + result.min);


    }
}
