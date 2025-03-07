package com.example.design.小傅哥拼团架构.责任链模式;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public abstract class AbstractLogicLink<R> implements  ILogicLink<R> {

    private ILogicLink<R> next;

    @Override
    public ILogicLink next() {
        return next;
    }

    @Override
    public ILogicLink appendNext(ILogicLink next) {
        this.next=next;
        return next;
    }

    protected R next(RequestAttributes requestAttributes, ApplicationContext dynamicContext){
        return next.apply(requestAttributes,dynamicContext);
    }
}
