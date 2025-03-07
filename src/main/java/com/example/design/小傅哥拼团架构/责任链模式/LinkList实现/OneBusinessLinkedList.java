package com.example.design.小傅哥拼团架构.责任链模式.LinkList实现;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public class OneBusinessLinkedList implements ILogicHandler<java.lang.String> {


    @Override
    public String apply(RequestAttributes requestAttributes, ApplicationContext dynamicContext) {
        System.out.println("OneBusinessLinkedList");
        return next(requestAttributes,dynamicContext);
    }
}
