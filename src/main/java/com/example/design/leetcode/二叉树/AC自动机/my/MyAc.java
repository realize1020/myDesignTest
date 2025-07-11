package com.example.design.leetcode.二叉树.AC自动机.my;

import com.example.design.leetcode.二叉树.AC自动机.返回匹配成功的数量.Solution;

import java.util.LinkedList;
import java.util.Queue;

public class MyAc {

    private Node root;

    static class Node {
        int end;
        Node fail;
        Node [] next;

        public Node(){
            next=new Node[26];
        }
    }

    public MyAc(){
        root = new Node();
    }

    public void insert(String str){
        char[] chars = str.toCharArray();
        Node cur = root;
        for(int i=0;i<chars.length;i++){
            int index = chars[i] - 'a';
            Node node =new Node();
            if(cur.next[index]==null){
                cur.next[index]=node;
            }
            cur = cur.next[index];//无论是有重复的路径还是新增完新路径之后都需要处理下一个

        }
        cur.end++;

    }


    public void build(){
        Queue<MyAc.Node> queue =new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node cur = queue.poll();
            for(int i=0;i<26;i++){
                if(cur.next[i]!=null){
                    cur.next[i].fail=root;
                    Node failNode = cur.fail;
                    while(failNode!=null){
                        if(failNode.next[i]!=null){
                            cur.next[i].fail=failNode.next[i];
                            break;
                        }
                        failNode = failNode.fail;

                    }
                    queue.add(cur.next[i]);
                }

            }
        }
    }

    public static void main(String[] args) {
        MyAc ac = new MyAc();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("c");
//        ac.insert("dheb");
        ac.build();
        System.out.println(ac);
    }

}
