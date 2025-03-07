package com.example.design.小傅哥拼团架构.责任链模式;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.多线程异步试算.RequestAttributes;

public interface ILogicLink<R> extends ILogicChainArmory{

    R apply(RequestAttributes requestAttributes , ApplicationContext dynamicContext);

}
