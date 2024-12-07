package com.example.design.leetcode.链表.反转链表;

import com.example.design.leetcode.ListNode;

public class Solution {

    public static void main(String[] args) {
        ListNode listNode1=new ListNode(1);
        ListNode listNode2=new ListNode(2);
        ListNode listNode3=new ListNode(3);
        ListNode listNode4=new ListNode(4);


        listNode1.next=listNode2;
        listNode2.next=listNode3;
        listNode3.next=listNode4;

        ListNode listNode = reverseListNode2(listNode1);

        while(listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }

    }



    public static ListNode reverseListNode(ListNode head){
        if(head==null){
            return null;
        }
        ListNode current =head;
        ListNode next =head.next;
        current.next=null; //最先翻转一次，置为null
        while(next!=null){//不能用next.next;
            ListNode temp=next.next;
            next.next=current;
            current=next;//这一步看，每次循环是current代替了next。所以最后current中存的才是完整的反转路径，要返回current，所有current就是头节点，所以while终止条件也是current.next=null,也就是next=null
            next=temp;
        }
        return current;//最后两个节点中前一个节点存的是完整信息
    }

    /**
     * leetcode官方
     * @param head
     * @return
     */
    public static ListNode reverseListNode2(ListNode head){
        ListNode pre=null;
        ListNode current =null;
        while(head!=null){ //head!=null不是head.next
            current = head.next;
            head.next=pre;
            pre=head;
            head=current;
        }
        return pre;
    }
}
