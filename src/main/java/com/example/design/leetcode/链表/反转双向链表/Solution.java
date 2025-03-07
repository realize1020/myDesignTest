package com.example.design.leetcode.链表.反转双向链表;

import com.example.design.leetcode.DoubleNode;

public class Solution {

    public static void main(String[] args) {
        DoubleNode DoubleNode1=new DoubleNode(1);
        DoubleNode DoubleNode2=new DoubleNode(2);
        DoubleNode DoubleNode3=new DoubleNode(3);
        DoubleNode DoubleNode4=new DoubleNode(4);


        DoubleNode1.next=DoubleNode2;
        DoubleNode2.last=DoubleNode1;

        DoubleNode2.next=DoubleNode3;
        DoubleNode3.last=DoubleNode2;

        DoubleNode3.next=DoubleNode4;
        DoubleNode4.last=DoubleNode3;

        DoubleNode doubleNode = reverseDoubleDoubleNode(DoubleNode1);

        while(doubleNode!=null){
            System.out.println(doubleNode.value);
            doubleNode=doubleNode.next;
        }

    }



    public static DoubleNode reverseDoubleDoubleNode(DoubleNode head){
        DoubleNode pre =null;
        DoubleNode next=null;
        while(head!=null){
            next = head.next;
            head.next=pre;
            head.last=next;//比单链表多了这行
            pre=head;
            head=next;
        }
        return pre;
    }


}
