package com.example.design.leetcode.链表.合并K个升序链表;

import com.example.design.leetcode.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 给你一个链表数组，每个链表都已经按升序排列。
 *
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */
public class Solution {

    public static class ListNodeComparator implements Comparator<ListNode> {

        @Override
        public int compare(ListNode o1, ListNode o2) {
            return o1.val - o2.val;
        }
    }

    /**
     * 左程云
     * @param lists
     * @return
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null) {
            return null;
        }
        PriorityQueue<ListNode> heap = new PriorityQueue<>(new ListNodeComparator());
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.add(lists[i]);
            }
        }
        if (heap.isEmpty()) {
            return null;
        }
        ListNode head = heap.poll();
        ListNode pre = head;
        if (pre.next != null) {
            heap.add(pre.next);
        }
        while (!heap.isEmpty()) {
            ListNode cur = heap.poll();
            pre.next = cur;
            pre = cur;
            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        return head;
    }


    public static ListNode mergeKLists2(ListNode[] lists){
        if(lists==null||lists.length==0){
            return null;
        }

        PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(new ListNodeComparator());
        ListNode dummy =new ListNode(0);
        ListNode p = dummy;
        for(ListNode node:lists){
            if(node!=null){
                queue.add(node);
            }
        }

        while(!queue.isEmpty()){
            p.next = queue.poll();
            p=p.next;
            if(p.next!=null){
                queue.add(p.next);
            }
        }
        return dummy.next;

    }


    public static void main(String[] args) {
        ListNode listNode1=new ListNode(1);
        ListNode listNode2=new ListNode(4);
        ListNode listNode22=new ListNode(5);

        ListNode listNode3=new ListNode(1);
        ListNode listNode4=new ListNode(3);
        ListNode listNode6=new ListNode(4);

        ListNode listNode5=new ListNode(2);
        ListNode listNode7=new ListNode(6);

        listNode1.next=listNode2;
        listNode2.next=listNode22;

        listNode3.next=listNode4;
        listNode4.next=listNode6;

        listNode5.next=listNode7;

        ListNode [] nodes ={listNode1,listNode5,listNode3};

        ListNode head = mergeKLists2(nodes);
        head.printList(head);


    }
}
