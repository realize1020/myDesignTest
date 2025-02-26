package com.example.design.小傅哥拼团架构.多线程异步试算;


public interface StrategyMapper<R> {

    StrageHandler<R> get(RequestAttributes attributes, ApplicationContext context);

}
