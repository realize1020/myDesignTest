package com.example.design.leetcode.链表.单链表构造队列;

import com.example.design.leetcode.ListNode;

public class Solution {

    //可以使用泛型来构造通用数据
    public static class MyQeueu{

        private ListNode head;

        private ListNode tail;

        private int size =0;

        public MyQeueu(){
            head=null;
            tail=null;
            size = 0;
        }

        public boolean isEmpty(){
            return size==0;
        }

        public void off(int val){
            ListNode cur =new ListNode(val);
            if(tail==null){
                head=cur;
                tail=cur;
            }else{
                tail.next=cur;
                tail=cur;
            }
            size++;
        }

        public int poll(){
            int ans = -1;
            if(head!=null){
                ans = head.val;
                head =head.next;
                size--;
            }else{
                tail=null;
            }
            return ans;
        }

        public int peek(){
            int ans = -1;
            if(head!=null){
                ans = head.val;
            }
            return ans;
        }


    }


    public static void main(String[] args) {
        MyQeueu myQeueu =new MyQeueu();
        myQeueu.off(1);
        myQeueu.off(2);
        myQeueu.off(3);
        System.out.println("数量"+myQeueu.size);

        System.out.println(myQeueu.poll());
        System.out.println(myQeueu.poll());
        System.out.println(myQeueu.poll());

    }


}

