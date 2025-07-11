package com.example.design.leetcode.二叉树.二叉树的最小深度;

import com.example.design.leetcode.二叉树.TreeNode;

/**
 * 返回x为头的树，最小深度是多少
 */
public class Solution {

    public static int minHeight(TreeNode treeNode){
        if(treeNode == null){
            return -1;
        }
        return p(treeNode);
    }

    /**
     * 方式一，递归，搜集左右最小的深度加上自身的1层
     * @param treeNode
     * @return
     */
    private static int p(TreeNode treeNode) {
        if(treeNode.right == null && treeNode.left == null){
            return 1;
        }
        int leftHeight = Integer.MAX_VALUE;
        int rightHeight= Integer.MAX_VALUE;

        if(treeNode.left != null){
            leftHeight = p(treeNode.left);
        }
        if(treeNode.right != null){
            rightHeight = p(treeNode.right);
        }

        return Math.min(leftHeight,rightHeight)+1;
    }

    /**
     * morris改写
     * @param head
     * @return
     */
    public static int minHeight2(TreeNode head) {
        if (head == null) return 0;  // 空树深度为0
        TreeNode cur = head;         // 当前节点
        TreeNode mostRight = null;   // 当前节点的左子树的最右节点
        int curLevel = 0;            // 当前节点的深度
        int minHeight = Integer.MAX_VALUE; // 最小深度初始化为最大值
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {  // 情况1：当前节点有左子树
                // 计算左子树最右节点的深度偏移量
                int rightBoardSize = 1;
                while (mostRight.right != null && mostRight.right != cur) {
                    rightBoardSize++;
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {  // 第一次访问（线索化）
                    curLevel++;                 // 进入左子树，深度+1
                    mostRight.right = cur;      // 建立线索
                    cur = cur.left;             // 继续遍历左子树
                    continue;
                } else {  // 第二次访问（已线索化，恢复树结构）
                    if (mostRight.left == null) {  // 如果最右节点是叶子节点
                        minHeight = Math.min(minHeight, curLevel);  // 更新最小深度
                    }
                    curLevel -= rightBoardSize;    // 回退深度（因为左子树已遍历完）
                    mostRight.right = null;       // 恢复树结构
                }
            } else {  // 情况2：当前节点无左子树（只有右子树）
                curLevel++;  // 进入右子树，深度+1
            }
            cur = cur.right;  // 继续遍历右子树
        }
        int finalRight = 1;
        cur = head;
        while (cur.right != null) {  // 计算最右侧路径的深度
            finalRight++;
            cur = cur.right;
        }
        if (cur.left == null && cur.right == null) {  // 如果最右节点是叶子节点
            minHeight = Math.min(minHeight, finalRight);  // 更新最小深度
        }
        return minHeight;
    }


    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(1,new TreeNode(2,new TreeNode(4),new TreeNode(5)),new TreeNode(3,null,null));
        int result = minHeight2(treeNode);
        System.out.println(result);
    }


}
