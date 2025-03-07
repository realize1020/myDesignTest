package com.example.design.小傅哥拼团架构.责任链模式.LinkList实现;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

//import java.util.LinkedList;

public class BusinessLinkedList<R> extends LinkedList<ILogicHandler<R>> implements ILogicHandler<R>{

    public BusinessLinkedList(String name) {
        super(name);
    }

    @Override
    public R apply(RequestAttributes requestAttributes, ApplicationContext dynamicContext) {
        Node<ILogicHandler<R>> first = this.first;
        while(first!=null){
            ILogicHandler<R> current=first.item;
            R apply = current.apply(requestAttributes,dynamicContext);
            if (null != apply) {
                return apply;
            }
            first=first.next;
        }

        return null;
    }
}
