package com.example.design.leetcode.链表.单链表构造栈;

import com.example.design.leetcode.ListNode;

public class Solution {

    public static class MyStack{

        private ListNode next;

        private int size;

        public MyStack(){
            next=null;
            size=0;
        }

        public void push(int val){
            ListNode cur =new ListNode(val);
            if(next==null){
                next = cur;

            }else{
                cur.next=next;
                next =cur;
            }
            size++;
        }

        public int pop(){
            int ans = -1;
            if(next!=null){
                ans = next.val;
                next =next.next;
                size--;
            }
            return ans;

        }
    }

    public static void main(String[] args) {
        MyStack myStack =new MyStack();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);

        System.out.println("数量："+myStack.size);

        System.out.println(myStack.pop());
        System.out.println(myStack.pop());
        System.out.println(myStack.pop());
    }
}
