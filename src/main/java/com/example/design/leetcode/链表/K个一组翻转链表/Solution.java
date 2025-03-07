package com.example.design.leetcode.链表.K个一组翻转链表;

import com.example.design.leetcode.ListNode;

import java.util.List;

/**
 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 *
 * 输入：head = [1,2,3,4,5], k = 2
 * 输出：[2,1,4,3,5]
 *
 * 输入：head = [1,2,3,4,5], k = 3
 * 输出：[3,2,1,4,5]
 */
public class Solution {

    public static void main(String[] args) {
        ListNode listNode1=new ListNode(1);
        ListNode listNode2=new ListNode(2);
        ListNode listNode3=new ListNode(3);
        ListNode listNode4=new ListNode(4);
        ListNode listNode5=new ListNode(5);


        listNode1.next=listNode2;
        listNode2.next=listNode3;
        listNode3.next=listNode4;
        listNode4.next=listNode5;


        //ListNode head = reverseKGroup4(listNode1, 2);
        ListNode head = reverseKGroupReplica(listNode1,2);
        head.printList(head);
    }

    /**
     * 2025年3月3日，手写一遍。1、判断head，2、找到移动k为之后的结点。一定要返回要反转结点的下一个结点。3、从开始节点到第二步返回的结点进行反转。4、原始的头节点指向下一次递归
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroupReplica(ListNode head, int k){
        int count = k;
        if(head==null || head.next==null){
            return head;
        }
        ListNode current=head;
        while(k>0){
            k--;
            current=current.next;
        }
        //反转这个范围的值
        ListNode originHead=head;
        ListNode pre=null;
        ListNode next=null;
        while(head!=current){
            next = head.next;
            head.next=pre;
            pre=head;
            head=next;
        }
        originHead.next=reverseKGroupReplica(current,count);
        return pre;
    }

    /** 递归法
     * 记得：最后一组不满足K个时不能再逆序操作
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup(ListNode head, int k) {
        if(head==null||head.next==null){
            return head;
        }
        ListNode pre=null;
       //先找到分成k组，每一组的末尾节点位置
        ListNode end = findEnd(head,k);//end返回的是下一个分组的起始节点
        if(end==head){//如果最后一组不够k个，不需要反转直接返回头节点
            return head;
        }
        pre=reverse(head,end);
        head.next=reverseKGroup(end,k);

        return pre;
    }

    /**
     * end要是k分后的链表的下一个结点
     * @param start
     * @param end
     * @return
     */
    private static ListNode reverse(ListNode start, ListNode end) {
        ListNode pre =null;
        ListNode next =null;
        while(start!=end){
            next = start.next;
            start.next=pre;
            pre=start;
            start =next;
        }
        return pre;
    }

    /**
     * 最后一组不满足K个时不能再逆序操作，返回原链表
     * @param head
     * @param k
     * @return
     */
    private static ListNode findEnd2(ListNode head, int k) {
        while(--k>=0&&head!=null){
            head=head.next;
        }
        return head;
    }

    /**
     * 迭代法
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup2(ListNode head, int k) {
        if(head==null||head.next==null){
            return head;
        }
        ListNode pre=null;
        ListNode lastStart=head;
        //先找到分成k组，每一组的末尾节点位置
        ListNode end = findEnd(head,k);
        if(end==head){//如果最后一组不够k个，不需要反转直接返回头节点
            return head;
        }
        pre=reverse(head,end);
        lastStart.next=end;
        while(end!=null){
            head=end;
            end = findEnd(end,k);
            if(end==head){//如果最后一组不够k个，不需要反转直接返回头节点
                lastStart.next=head;
                return pre;
            }
            lastStart.next=reverse(head,end);
            lastStart=head;
        }

        return pre;
    }


    private static ListNode findEnd(ListNode head, int k) {
        ListNode current = head;
        while(--k>=0&&head!=null){
            head=head.next;
        }
        if(k!=-1){//说明这一组不足k个，head在后移过程中还不足k个就已经为null了
            return current;
        }
        return head;
    }




    public static ListNode reverseKGroup3(ListNode head, int k){
        if(head==null||head.next==null){
            return head;
        }
        // 创建一个虚拟头节点，方便处理边界情况
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // 用于遍历的指针
        ListNode prev = dummy;
        ListNode end = dummy;
        while(end!=null){
            end = findEnd2(end,k);
            if(end==null){//如果最后一组不够k个，不需要反转直接返回头节点
                break;
            }
            // 记录下一个子链表的起始节点
            ListNode start = prev.next;
            ListNode next = end.next;
            end.next = null; // 断开子链表

            // 翻转子链表
            prev.next = reverse(start,end.next);

            // 将翻转后的子链表与后续链表连接
            start.next = next;

            // 更新 prev 和 end 指针，准备处理下一个子链表
            prev = start;
            end = prev;
        }

        return dummy.next;
    }

    /**
     * 迭代法
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup4(ListNode head, int k) {
        if(head==null||head.next==null){
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next=head;
        ListNode start =dummy.next;
        ListNode next =dummy.next;
        ListNode pre=dummy;

        ListNode end=next;
        while(end!=null){
            end = findEnd(end,k);
            if(end==null){//如果最后一组不够k个，不需要反转直接返回头节点
                break;
            }
            pre.next=reverse(start,end);
            start.next=end;
            start=end;
            pre=start;
        }

        return dummy.next;
    }


}
