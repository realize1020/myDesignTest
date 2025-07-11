package com.example.design.leetcode.二叉树.morris遍历;

import com.example.design.leetcode.二叉树.TreeNode;

import java.util.Stack;

/**
 * morris遍历是二叉树遍历算法的超强进阶算法，跟递归、非递归（栈实现）的空间复杂度，
 * morris遍历可以将非递归遍历中的空间复杂度降为O(1)。从而实现时间复杂度为O(N)，而空间复杂度为O(1)的精妙算法。
 * morris遍历利用的是树的叶节点左右孩子为空（树的大量空闲指针），实现空间开销的极限缩减。
 */
public class Solution {
    public static void morris(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            cur = cur.right;
        }
    }

    /**
     * 第二种写法
     * @param head
     */
    public static void morris2(TreeNode head) {
        if (head == null) return;
        TreeNode cur = head;
        while (cur != null) {
            System.out.println(cur.val);
            if (cur.left == null) {
                cur = cur.right;
            } else {
                TreeNode mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    mostRight.right = null;
                    cur = cur.right;
                }
            }
        }
    }

    /**
     * 二、前序遍历
     * 前序遍历与中序遍历相似，代码上只有一行不同，不同就在于输出的顺序。
     * 步骤：
     *
     * 如果当前节点的左孩子为空，则输出当前节点并将其右孩子作为当前节点。
     *
     * 如果当前节点的左孩子不为空，在当前节点的左子树中找到当前节点在中序遍历下的前驱节点。
     *
     * a) 如果前驱节点的右孩子为空，将它的右孩子设置为当前节点。输出当前节点（在这里输出，这是与中序遍历唯一一点不同）。当前节点更新为当前节点的左孩子。
     *
     * b) 如果前驱节点的右孩子为当前节点，将它的右孩子重新设为空。当前节点更新为当前节点的右孩子。
     *
     * 重复以上1、2直到当前节点为空。
     * @param head
     */
    public static void morrisPre(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {//在以下两种情况下打印节点：当节点没有左子树时，打印当前节点；当节点有左子树时并且第一次访问时打印该节点。那么何时是第一次访问呢？就是当mosrRight的右指针为null时。
                    System.out.print(cur.val + " ");
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else {
                System.out.print(cur.val + " ");
            }
            cur = cur.right;
        }
        System.out.println();
    }

    /**
     * 将Morris遍历改中序遍历
     * 在以下两种情况下打印节点:
     *
     * 当节点没有左子树时，打印当前节点；
     * 当节点有左子树时并且第二次访问时打印该节点。那么何时是第二次访问呢？就是当mosrRight的右指针不为null时。
     * @param head
     */
    public static void morrisIn(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            System.out.print(cur.val + " ");
            cur = cur.right;
        }
    }

    public static void morrisIn2(TreeNode head) {
        if (head == null) return;
        TreeNode cur = head;
        while (cur != null) {
            if (cur.left == null) {
                System.out.println(cur.val);//情况一：如果一个节点没有左子树时打印
                cur = cur.right;
            } else {
                TreeNode mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    System.out.println(cur.val);//情况二：如果有左子树且第二次来到左子树时打印
                    mostRight.right = null;
                    cur = cur.right;
                }
            }
        }
    }

    /**
     * 将Morris遍历改后序遍历
     * 在以下两种情况下打印节点:
     *
     * 只有当到达某个节点两次时逆序打印该节点左子树的右边界;
     * 在代码的最后逆序打印整棵树的右边界，而逆序的过程就和单向链表的反转过程类似
     * @param head
     */
    public static void morrisPost(TreeNode head) {
        if (head == null) {
            return;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                    //情况二：如果有左子树且第二次来到左子树时打印
                    TreeNode left = cur.left;
                    printInOrder(left);
                }
            }
            cur = cur.right;
        }
        //情况三：逆序打印整棵树的右边界，而逆序的过程就和单向链表反转过程类似
        printInOrder(head);
    }

    private static void printInOrder(TreeNode left) {

        TreeNode current1 = reverseNode(left);

        //TreeNode current1 = current;
        while(current1!=null){
            System.out.print(current1.val+" ");
            current1=current1.right;
        }

    }

    /**
     * 链表反转实现
     * @param left
     * @return
     */
    private static TreeNode reverseNode(TreeNode left) {
        TreeNode pre = null;
        TreeNode next = null;
        TreeNode current = left;
        while(current!=null){
            next= current.right;
            current.right=pre;
            pre= current;
            current=next;
        }
        return pre;
    }

    /**
     *栈实现
     * @param left
     */
    private static void printInOrder2(TreeNode left) {
        Stack stack =new Stack<TreeNode>();
        while(left!=null){
            stack.push(left);
            left=left.right;
        }
        while(!stack.isEmpty()){
            TreeNode pop = (TreeNode) stack.pop();
            System.out.print(pop.val+" ");
        }
    }

    /**
     * morris遍历用途，判断二叉搜索树，
     * BST 的性质：左子树的最大值小于当前节点值，右子树的最小值大于当前节点值
     *
     * 正常方法的话是递归调用判断或者中序遍历后，一直升序就是搜索二叉树。
     * @param head
     * @return
     */
    public static boolean isBST(TreeNode head) {
        if (head == null) {
            return true;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        Integer pre = null;
        boolean ans = true;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            if (pre != null && pre >= cur.val) {
                ans = false;
            }
            pre = cur.val;
            cur = cur.right;
        }
        return ans;
    }


    /**
     * morris改写求最小深度
     * @param head
     * @return
     */
    public static int minHeight2(TreeNode head) {
        if (head == null) {
            return 0;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        int curLevel = 0;
        int minHeight = Integer.MAX_VALUE;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                int rightBoardSize = 1;
                while (mostRight.right != null && mostRight.right != cur) {
                    rightBoardSize++;
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) { // 第一次到达
                    curLevel++;
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else { // 第二次到达
                    if (mostRight.left == null) {  // 如果最右节点是叶子节点
                        minHeight = Math.min(minHeight, curLevel);  // 更新最小深度
                    }
                    curLevel -= rightBoardSize;// 回退深度（因为左子树已遍历完）
                    mostRight.right = null;
                }
            } else { // cur节点只有右孩子，所以当前level+1
                curLevel++;
            }
            cur = cur.right;
        }
        int finalRight = 1;
        cur = head;
        while (cur.right != null) {
            finalRight++;
            cur = cur.right;
        }
        if (cur.left == null && cur.right == null) {
            minHeight = Math.min(minHeight, finalRight);
        }
        return minHeight;
    }

    public static void main(String[] args) {
        TreeNode treeNode =new TreeNode(1,new TreeNode(4,new TreeNode(5),new TreeNode(6)),new TreeNode(2,new TreeNode(3),null));
        morrisIn(treeNode);
        System.out.println();
        morrisPost(treeNode);
    }

}
