package com.example.design.小傅哥拼团架构.多线程异步试算;

public interface StrageHandler<R> {

    StrageHandler defaultStrageHandler =(attributes, context) -> null;

    R apply(RequestAttributes attributes, ApplicationContext context) throws Exception;

}
