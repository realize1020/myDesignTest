package com.example.design.小傅哥拼团架构.责任链模式;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public class TestFactory{

    private LinkOne linkOne =new LinkOne();

    private LinkTwo linkTwo =new LinkTwo();

    public ILogicLink<String> openLink(){
        linkOne.appendNext(linkTwo);
        return linkOne;
    }

    public static void main(String[] args) {
        TestFactory testFactory =new TestFactory();
        ILogicLink<String> link = testFactory.openLink();
        String result = link.apply(new RequestAttributes(), new ApplicationContext());
        System.out.println(result);
    }
}
