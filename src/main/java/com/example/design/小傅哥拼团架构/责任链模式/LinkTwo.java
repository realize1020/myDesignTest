package com.example.design.小傅哥拼团架构.责任链模式;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public class LinkTwo extends AbstractLogicLink<String>{
    @Override
    public String apply(RequestAttributes requestAttributes, ApplicationContext dynamicContext) {
        System.out.println("222222222222222222222");
        return "单实例链";
    }
}
