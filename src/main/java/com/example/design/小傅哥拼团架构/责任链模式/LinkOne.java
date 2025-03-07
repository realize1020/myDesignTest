package com.example.design.小傅哥拼团架构.责任链模式;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public class LinkOne extends AbstractLogicLink<String>{
    @Override
    public String apply(RequestAttributes requestAttributes, ApplicationContext dynamicContext) {
        System.out.println("111111111111111111111111");
        return next(requestAttributes,dynamicContext);
    }
}
