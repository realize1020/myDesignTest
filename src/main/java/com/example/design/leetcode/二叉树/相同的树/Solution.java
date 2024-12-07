package com.example.design.leetcode.二叉树.相同的树;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 * 输入：p = [1,2,3], q = [1,2,3]
 * 输出：true
 *
 * 输入：p = [1,2], q = [1,null,2]
 * 输出：false
 */
public class Solution {

    /** 深度优先搜索
     * 思路：递归调用。一棵树是否相同，它的左子树是否相同、它的右子树是否相同、自身根节点是否相同。
     * @param p
     * @param q
     * @return
     */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if(p==null&&q==null){
            return true;
        }
//        if((p==null&&q!=null)||(q==null&&p!=null)){
//            return false;
//        }
        if(p==null ^ q==null){
            return false;
        }

        return p.val==q.val&&isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);

    }

    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(1);
        TreeNode treeNode2 =new TreeNode(2);
        TreeNode treeNode3 =new TreeNode(3);

        TreeNode treeNode4 =new TreeNode(1);
        TreeNode treeNode5 =new TreeNode(2);
        TreeNode treeNode6 =new TreeNode(3);

        treeNode.left=treeNode2;
        treeNode.right=null;

        treeNode4.left=null;
        treeNode4.right=treeNode6;


        boolean sameTree = isSameTree(treeNode, treeNode4);
        System.out.println(sameTree);
    }

    /**
     * 方法二：广度优先搜索
     * 也可以通过广度优先搜索判断两个二叉树是否相同。同样首先判断两个二叉树是否为空，如果两个二叉树都不为空，则从两个二叉树的根节点开始广度优先搜索。
     *
     * 使用两个队列分别存储两个二叉树的节点。初始时将两个二叉树的根节点分别加入两个队列。每次从两个队列各取出一个节点，进行如下比较操作。
     *
     * 比较两个节点的值，如果两个节点的值不相同则两个二叉树一定不同；
     *
     * 如果两个节点的值相同，则判断两个节点的子节点是否为空，如果只有一个节点的左子节点为空，或者只有一个节点的右子节点为空，则两个二叉树的结构不同，因此两个二叉树一定不同；
     *
     * 如果两个节点的子节点的结构相同，则将两个节点的非空子节点分别加入两个队列，子节点加入队列时需要注意顺序，如果左右子节点都不为空，则先加入左子节点，后加入右子节点。
     *
     * 如果搜索结束时两个队列同时为空，则两个二叉树相同。如果只有一个队列为空，则两个二叉树的结构不同，因此两个二叉树不同。
     *
     * 作者：力扣官方题解
     * 链接：https://leetcode.cn/problems/same-tree/solutions/363636/xiang-tong-de-shu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param p
     * @param q
     * @return
     */
    public static boolean isSameTree2(TreeNode p, TreeNode q){
        if (p == null && q == null) {
            return true;
        } else if (p == null || q == null) {
            return false;
        }
        Queue<TreeNode> queue1 = new LinkedList<TreeNode>();
        Queue<TreeNode> queue2 = new LinkedList<TreeNode>();
        queue1.offer(p);
        queue2.offer(q);
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            TreeNode node1 = queue1.poll();
            TreeNode node2 = queue2.poll();
            if (node1.val != node2.val) {
                return false;
            }
            TreeNode left1 = node1.left, right1 = node1.right, left2 = node2.left, right2 = node2.right;
            if (left1 == null ^ left2 == null) {
                return false;
            }
            if (right1 == null ^ right2 == null) {
                return false;
            }
            if (left1 != null) {
                queue1.offer(left1);
            }
            if (right1 != null) {
                queue1.offer(right1);
            }
            if (left2 != null) {
                queue2.offer(left2);
            }
            if (right2 != null) {
                queue2.offer(right2);
            }
        }
        return queue1.isEmpty() && queue2.isEmpty();
    }
}
