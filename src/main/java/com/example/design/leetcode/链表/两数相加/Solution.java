package com.example.design.leetcode.链表.两数相加;

import com.example.design.leetcode.ListNode;

/**
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807.
 *
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 * 输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * 输出：[8,9,9,9,0,0,0,1]
 *
 * 每个链表中的节点数在范围 [1, 100] 内
 *
 * 两种方法：1、使用一个预先的伪指针。2、直接
 */
public class Solution {
    public static void main(String[] args) {
//        ListNode listNode1=new ListNode(3);
//        ListNode listNode2=new ListNode(4);
//        ListNode listNode3=new ListNode(2);
//
//        ListNode listNode4=new ListNode(5);
//        ListNode listNode5=new ListNode(6);
//        ListNode listNode6=new ListNode(4);

//        listNode1.next=listNode2;
//        listNode2.next=listNode3;
//
//        listNode4.next=listNode5;
//        listNode5.next=listNode6;
//

        ListNode listNode1=new ListNode(9);
        ListNode listNode2=new ListNode(9);
        ListNode listNode3=new ListNode(9);
        ListNode listNode4=new ListNode(9);

        ListNode listNode5=new ListNode(9);
        ListNode listNode6=new ListNode(9);
        ListNode listNode7=new ListNode(9);
        ListNode listNode8=new ListNode(9);
        ListNode listNode9=new ListNode(9);
        ListNode listNode10=new ListNode(9);
        ListNode listNode11=new ListNode(9);


        listNode1.next=listNode2;
        //listNode2.next=listNode3;
        //listNode3.next=listNode4;

        listNode5.next=listNode6;
        listNode6.next=listNode7;
        listNode7.next=listNode8;
        //listNode8.next=listNode9;
        //listNode9.next=listNode10;
        //listNode10.next=listNode11;





        ListNode head = addTwoNumbers2(listNode1, listNode5);

        head.printList(head);
    }

    public static ListNode addTwoNumbers2(ListNode head1, ListNode head2) {
        //1、先判断谁长谁短，找出短的链表
        int length1 = listLength(head1);
        int length2=listLength(head2);
        ListNode longListNode=null;
        ListNode shortListNode=null;
        ListNode longList=null;
        if(length1>length2){
            longListNode=head1;
            shortListNode=head2;
        }else{
            longListNode=head2;
            shortListNode=head1;
        }
        longList=longListNode;
        int carry=0;
        int sum=0;
        ListNode next=null; //为了最后连接进位carry，因为longList此时由于最后又后移一位，已经为null,无法连接carry
        while(shortListNode!=null){
            sum=shortListNode.val+longList.val+carry;
            longList.val = sum % 10;
            carry = sum/10;
            next=longList;
            longList=longList.next;
            shortListNode=shortListNode.next;

        }

        while(longList!=null){
            sum=longList.val+carry;
            longList.val = sum % 10;
            carry = sum/10;
            next=longList;
            longList=longList.next;
        }
        //最后不要忘了可能进位还有1
        if(carry!=0){
            //longList.next=new ListNode(carry);//longList为null，无法
            next.next=new ListNode(carry);

        }
        return longListNode;

    }

    /**
     * 左程云
     * @param head1
     * @param head2
     * @return
     */
    public static ListNode addTwoNumbers(ListNode head1, ListNode head2) {
        int len1 = listLength(head1);
        int len2 = listLength(head2);
        ListNode l = len1 >= len2 ? head1 : head2; //l表示长链表
        ListNode s = l == head1 ? head2 : head1;//s表示短链表
        ListNode curL = l;
        ListNode curS = s;
        ListNode last = curL;
        int carry = 0;//进位的值
        int curNum = 0;
        while (curS != null) {//要从短的开始
            curNum = curL.val + curS.val + carry;
            curL.val = (curNum % 10);
            carry = curNum / 10;
            last = curL;
            curL = curL.next;
            curS = curS.next;
        }
        while (curL != null) {
            curNum = curL.val + carry;
            curL.val = (curNum % 10);
            carry = curNum / 10;
            last = curL;
            curL = curL.next;
        }
        if (carry != 0) {
            last.next = new ListNode(1);
        }
        return l;
    }

    // 求链表长度
    public static int listLength(ListNode head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }
}
