package com.example.design.leetcode.链表.合并两个有序链表;

import com.example.design.leetcode.ListNode;

public class Solution {
    public static void main(String[] args) {
        ListNode listNode1=new ListNode(1);
        ListNode listNode2=new ListNode(2);
        ListNode listNode3=new ListNode(4);

        ListNode listNode4=new ListNode(1);
        ListNode listNode5=new ListNode(3);
        ListNode listNode6=new ListNode(4);


//        listNode1.next=listNode2;
//        listNode2.next=listNode3;
//
//        listNode4.next=listNode5;
//        listNode5.next=listNode6;

        ListNode head = mergeTwoLists(listNode2, listNode1);
        head.printList(head);
    }

    /**
     * 我的第一种方式，比较直接的思维方式。连个指针、一个指针指向原链表，一个指针指向新构造的合并链表，需要初始化一个预结点。
     * @param list1
     * @param list2
     * @return
     */
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        ListNode pre=new ListNode(-1);
        ListNode cur=pre;
        while(list1!=null&&list2!=null){
            if(list1.val<list2.val){ //谁小谁连到后面
                cur.next=list1;
                list1=list1.next; //
                cur=cur.next;
            }else{
                cur.next=list2;
                list2=list2.next;
                cur=cur.next;
            }

        }
        if(list1!=null){
            cur.next=list1;
        }else{
            cur.next=list2;
        }

        return pre.next;
    }


    /**
     * leetcode官方，递归法
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

}
