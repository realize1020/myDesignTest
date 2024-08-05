package com.example.design.leetcode.二叉树.对称二叉树;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.*;

/**
 * 输入：root = [1,2,2,3,4,4,3]
 * 输出：true
 * <p>
 * 输入：root = [1,2,2,null,3,null,3]
 * 输出：false
 *
 * [1,2,2,2,null,2]
 *
 * [5,4,1,null,1,null,4,2,null,2,null]
 */
public class Solution {

    /**
     * 感觉使用中序遍历的序列可以判断出来
     *
     * @param root
     * @return
     */
    public static boolean isSymmetric(TreeNode root) {
        List<Integer> list = inorderTraversal(root);
        //如果中序遍历序列list里面是偶数的话，就直接可以判断false
        if(list.size() % 2==0){
            return false;
        }

        System.out.println("中序遍历："+list);

        int rootValue = root.val;


        int index=list.size() / 2;
        int position = list.indexOf(rootValue);
        if(position!=index){
            return false;
        }
        if(root.left.val!=root.right.val){
            return false;
        }
        List<Integer> desList1= list.subList(0,index);
        List<Integer> desList2= list.subList(index+1,list.size());


        Collections.reverse(desList1);
        boolean right = true;
        for(int i=0;i<desList1.size();i++){
            if(desList1.get(i)!=desList2.get(i)){
                right = false;
                break;
            }
        }
        return right;

    }

    public static  List<Integer> inorderTraversal(TreeNode root) {
        if(root ==null){
            return null;
        }
        List<Integer> treeList =new ArrayList<>();
        //遍历左子树
        List<Integer> listLeft = inorderTraversal(root.left);
        if(listLeft==null){
            treeList.add(null);
        }else{
            treeList.addAll(listLeft);
        }
//        if(listLeft==null&&root.right!=null){
//            treeList.add(null);
//        }else if(listLeft==null&&root.right==null){
//
//        } else{
//            treeList.addAll(listLeft);
//        }

        //输入根节点
        Integer value = root.val;
        treeList.add(value);


        //遍历右子树
        List<Integer> listRight = inorderTraversal(root.right);
//        if(listLeft!=null&&listRight==null){
//            treeList.add(null);
//        }else if(listLeft==null&&listRight==null){
//
//        } else{
//            treeList.addAll(listRight);
//        }
        if(listRight==null){
            treeList.add(null);
        }else{
            treeList.addAll(listRight);
        }



        return treeList;
    }


    /**
     * 两个指针从根节点开始遍历，一个左移的同时另一个右移
     * @param root
     * @return
     */
    public static boolean isSymmetric2(TreeNode root) {
        TreeNode p=root;
        TreeNode q=root;
        Stack<TreeNode> stackp = new Stack<>();
        Stack<TreeNode> stackq = new Stack<>();
        while(p.left!=null&&q.right!=null){
            if(p.left.val==q.right.val){
                stackp.push(p.left);
                stackq.push(q.right);
                p=p.left;
                q=q.right;
            }else{
                return false;
            }
        }
        if(p.left!=null||q.right!=null){
            return false;
        }
//        while(p.right!=null&&q.left!=null){
//            if(p.left.val==q.right.val){
//                stackp.push(p.right);
//                stackq.push(q.left);
//                p=p.right;
//                q=q.left;
//            }else{
//                return false;
//            }
//
//        }
//        if(p.right!=null||q.left!=null){
//            return false;
//        }
        while(!stackp.isEmpty()&&!stackq.isEmpty()){
            p=stackp.pop();
            q=stackq.pop();
            while((p.right!=null&&q.left!=null)||(p.left!=null&&q.right!=null)){
                if(p.right!=null&&q.left==null){
                    return false;
                }else if(p.right==null&&q.left!=null){
                    return false;
                }
                if(p.right!=null&&q.left!=null&&p.right.val==q.left.val){
                    p=p.right;
                    q=q.left;
                    stackp.push(p);
                    stackq.push(q);
                }else if(p.left!=null&&q.right!=null&&p.left.val==q.right.val){
                    p=p.left;
                    q=q.right;
                    stackp.push(p);
                    stackq.push(q);
                }
                else{
                    return false;
                }

            }

        }

        return true;


    }


    /**
     * leetcode官方
     * @param root
     * @return
     */
    public static boolean isSymmetric3(TreeNode root) {
        return check3(root, root);
    }

    public static boolean check(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
    }

    public static boolean check2(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }

        if(p.val != q.val){
            return false;

        }
        return check2(p.left, q.right) && check2(p.right, q.left);

    }

    /**
     * 迭代法：使用队列
     * @param u
     * @param v
     * @return
     */
    public static boolean check3(TreeNode u, TreeNode v) {
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        q.offer(u);
        q.offer(v);
        while (!q.isEmpty()) {
            u = q.poll();
            v = q.poll();
            if (u == null && v == null) {
                continue;
            }
            if ((u == null || v == null) || (u.val != v.val)) {
                return false;
            }

            q.offer(u.left);
            q.offer(v.right);

            q.offer(u.right);
            q.offer(v.left);
        }
        return true;
    }

    /**
     * 利用两个栈和
     * @param root
     * @return
     */
    public static boolean isSymmetric4(TreeNode root){
        if (root == null) return true;

        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();

        // 前序遍历压栈
        stack1.push(root);
        // 反向前序遍历压栈，注意这里的处理顺序
        stack2.push(root);

        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            TreeNode node1 = stack1.pop();
            TreeNode node2 = stack2.pop();

            // 如果两个节点值不相等，则不对称
            if (node1.val != node2.val) return false;

            // 前序遍历：左->右，所以先压右孩子再压左孩子
            if (node1.right != null) stack1.push(node1.right);
            if (node1.left != null) stack1.push(node1.left);

            // 反向前序遍历：右->左，所以先压左孩子再压右孩子
            if (node2.left != null) stack2.push(node2.left);
            if (node2.right != null) stack2.push(node2.right);
        }

        // 如果两个栈都为空，说明树对称
        return stack1.isEmpty() && stack2.isEmpty();
    }




    public static void main(String[] args) {
        TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3,null,new TreeNode(7,null,null)), new TreeNode(4,new TreeNode(1,null,null),null)), new TreeNode(2, new TreeNode(4,null,new TreeNode(1,null,null)), new TreeNode(3,new TreeNode(6,null,null),null)));
        TreeNode root2 = new TreeNode(1, new TreeNode(2, null, new TreeNode(3)), new TreeNode(2, null, new TreeNode(3)));
        TreeNode root3 = new TreeNode(1, new TreeNode(2, null, null), new TreeNode(2, null, new TreeNode(3)));
        TreeNode root4 = new TreeNode(2, new TreeNode(3, new TreeNode(4,null,null), new TreeNode(5,null,null)), new TreeNode(3, null, new TreeNode(4,null,null)));

        boolean result = isSymmetric4(root);
        System.out.println(result);
    }
}
