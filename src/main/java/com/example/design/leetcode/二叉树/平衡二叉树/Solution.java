package com.example.design.leetcode.二叉树.平衡二叉树;

import com.example.design.leetcode.二叉树.TreeNode;

/**
 * 给定一个二叉树，判断它是否是平衡二叉树
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：true
 *
 * 输入：root = [1,2,2,3,3,null,null,4,4]
 * 输出：false
 *
 */
public class Solution {

    /**
     * 我的思路是用深度搜索遍历左右节点高度后，判断相差高度
     * @param root
     * @return
     */
    public static boolean isBalanced(TreeNode root) {
        if(root == null){
            return true;
        }
        int leftDepth = deepSearch(root.left);
        int rightDepth = deepSearch(root.right);
        if(leftDepth==rightDepth||Math.abs(leftDepth-rightDepth)==1){
            return isBalanced(root.left)&&isBalanced(root.right);
        }else if(leftDepth>rightDepth+1){
            return false;
        }else if(1+leftDepth<rightDepth){
            return false;
        }else{
            return true;
        }


    }

    /**
     * 更简洁一点，也可以使用deepSearch2，而且好的情况下（子树不平衡）可以直接判断出整棵树不平衡，
     * 但是，最差情况下（如果整棵树是平衡的话）还是会在求高度和这里递归两次
     * @param root
     * @return
     */
    public static boolean isBalanced3(TreeNode root) {
        if(root == null){
            return true;
        }
        int leftDepth = deepSearch2(root.left);
        int rightDepth = deepSearch2(root.right);
        if(leftDepth!=rightDepth&&Math.abs(leftDepth-rightDepth)!=1){
            return false;
        }else{
            return isBalanced(root.left)&&isBalanced(root.right);
        }
    }

    /**
     * 只需要在求高度的方法里面递归一次就好了,时间复杂度和空间复杂度都为O(n)
     * @param root
     * @return
     */
    public static boolean isBalanced4(TreeNode root) {
        int result = deepSearch2(root);
        return result>=0;
    }

    /**
     * 获取root节点的高度，不管子树是否符合平衡二叉树的规则，都先递归把树的高度求出来，然后再父方法里面再判断高度差，
     * 因为父方法和求高度的这个方法都递归了一遍，所以时间复杂度高
     * @param root
     * @return
     */
    private static int deepSearch(TreeNode root) {
        if(root == null){
            return 0;
        }
        int leftDepth = deepSearch(root.left);
        int rightDepth = deepSearch(root.right);
        return Math.max(leftDepth,rightDepth)+1;

    }

    /**
     * 自下而上判断高度时就判断子树是否符合平衡二叉树的特征，不符合直接返回-1
     * @param root
     * @return
     */
    private static int deepSearch2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = deepSearch2(root.left);
        int rightDepth = deepSearch2(root.right);
        //在这个方法里先提前判断高度
        if (leftDepth > rightDepth + 1) {
            return -1;
        } else if (1 + leftDepth < rightDepth) {
            return -1;
        } else if (leftDepth == -1 || rightDepth == -1) {
            return -1;
        } else {
            return Math.max(leftDepth, rightDepth) + 1;
        }
    }
    /**
     * 自下而上遍历每个节点的高度
     * @param root
     * @return
     */
    public boolean isBalanced2(TreeNode root) {
        return height(root) >= 0;
    }

    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        } else {
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }



    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(3,new TreeNode(9,null,null),new TreeNode(20,new TreeNode(15,null,null),new TreeNode(7,null,null)));
        TreeNode treeNode2 =new TreeNode(1,new TreeNode(2,new TreeNode(3,new TreeNode(4,null,null),new TreeNode(4,null,null)),new TreeNode(3,null,null)),new TreeNode(2,null,null));
        TreeNode treeNode3 =new TreeNode(1,null,null);
        TreeNode treeNode4 =new TreeNode(1,new TreeNode(2,new TreeNode(3,new TreeNode(4,null,null),null),null),new TreeNode(2,null,new TreeNode(3,null,new TreeNode(4,null,null))));
        boolean balanced = isBalanced(treeNode4);
        System.out.println(balanced);
    }


    // 左、右要求一样，Info 信息返回的结构体
    public static class Info {
        public boolean isBalaced;
        public int height;

        public Info(boolean b, int h) {
            isBalaced = b;
            height = h;
        }
    }


    public static Info process2(TreeNode X) {
        if (X == null) {
            return new Info(true, 0);
        }
        Info leftInfo = process2(X.left);
        Info rightInfo = process2(X.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isBalanced = true;
        if (!leftInfo.isBalaced || !rightInfo.isBalaced || Math.abs(leftInfo.height - rightInfo.height) > 1) {
            isBalanced = false;
        }
        return new Info(isBalanced, height);
    }
}
