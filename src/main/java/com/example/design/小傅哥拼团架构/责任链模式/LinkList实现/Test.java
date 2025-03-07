package com.example.design.小傅哥拼团架构.责任链模式.LinkList实现;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public class Test {

    public static void main(String[] args) {
        OneBusinessLinkedList businessLinkedList1 =new OneBusinessLinkedList();
        TwoBusinessLinkedList businessLinkedList2 =new TwoBusinessLinkedList();

        BusinessLinkedList businessLinkedList =new BusinessLinkedList("businessLinkedList");
        businessLinkedList.add(businessLinkedList1);
        businessLinkedList.add(businessLinkedList2);

        Object apply = businessLinkedList.apply(new RequestAttributes(), new ApplicationContext());
        System.out.println(apply.toString());

    }
}
