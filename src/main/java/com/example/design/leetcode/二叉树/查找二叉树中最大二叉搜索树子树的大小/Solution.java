package com.example.design.leetcode.二叉树.查找二叉树中最大二叉搜索树子树的大小;

import com.example.design.leetcode.二叉树.TreeBuilder;
import com.example.design.leetcode.二叉树.TreeNode;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;


public class Solution {
    public static void main(String[] args) {
        Integer nodes[] ={4,2,7,1,3,6,8};
        TreeNode treeNode = TreeBuilder.buildTree(nodes);
        //方法一
        System.out.println(maxSubBSTSize(treeNode));
        //方法二
        System.out.println(maxSubBSTSize2(treeNode).size);

//        int maxLevel = 3;
//        int maxValue = 100;
//        int testTimes = 100;
//        for (int i = 0; i < testTimes; i++) {
//            TreeNode head = generateRandomBST(maxLevel, maxValue);
//            int size1=maxSubBSTSize(head);
//            int size2 = maxSubBSTSize2(head).size;
//
//            if ( size1!= size2) {
//                System.out.println("maxSubBSTSize="+size1);
//                System.out.println("maxSubBSTSize2="+size2);
//                System.out.println("Oops!");
//            }
//        }
//        System.out.println("finish!");
    }


    // for test
    public static TreeNode generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    public static int maxSubBSTSize(TreeNode node){
        if(node==null){
            return 0;
        }
        int size=getBSTSize(node);
        if(size!=0){
            return size;
        }
        return Math.max(maxSubBSTSize(node.left),maxSubBSTSize(node.right));
    }

    public static int getBSTSize(TreeNode node){
        if(node==null){
            return 0;
        }
        ArrayList<Integer> ans = new ArrayList<>();
        inOrder(node,ans);
        for(int i=1;i<ans.size();i++){
            if(ans.get(i)<ans.get(i-1)){
                return 0;
            }
        }
        return ans.size();
    }

    private static void inOrder(TreeNode node, ArrayList ans) {
        if(node==null){
            return;
        }
        inOrder(node.left,ans);
        ans.add(node.val);
        inOrder(node.right,ans);
    }


    public static Info maxSubBSTSize2(TreeNode node){
        if(node==null){
            //return new Info(0,0,0,true);//下面也行
            return new Info(0,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
        }
        Info leftinfo = maxSubBSTSize2(node.left);
        Info rightinfo = maxSubBSTSize2(node.right);
        int currentVal=node.val;
        int max = currentVal;
        int min = currentVal;
        int size = 0;
        boolean isBST =false;
        if((leftinfo.isBST&&leftinfo.max<currentVal&&rightinfo.isBST&&rightinfo.min>currentVal)||(node.left==null&&node.right==null)){
            isBST=true;
        }

       max=Math.max(Math.max(max,leftinfo.max),rightinfo.max);
       min=Math.max(Math.max(min,leftinfo.min),rightinfo.min);
        if(isBST){
            size=leftinfo.size+rightinfo.size+1;
        }else{
            size=Math.max(Math.max(size,leftinfo.size),rightinfo.size);
        }
        return new Info(size,max,min,isBST);
    }

    public static int getTreeHeight(TreeNode node){
        if(node==null){
            return 0;
        }
        return Math.max(getTreeHeight(node.left),getTreeHeight(node.right))+1;
    }

    public static class Info{
        private int size;
        private int max;
        private int min;
        private boolean isBST;

        public Info(){}

        public Info(int size,int max,int min,boolean isBST){
            this.size=size;
            this.max=max;
            this.min=min;
            this.isBST=isBST;
        }
    }

    // for test
    public static TreeNode generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }
}
