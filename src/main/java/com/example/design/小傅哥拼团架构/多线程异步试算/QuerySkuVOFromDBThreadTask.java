package com.example.design.小傅哥拼团架构.多线程异步试算;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class QuerySkuVOFromDBThreadTask implements Callable<java.lang.String> {

    @Override
    public String call() throws Exception {
        log.info("查询商品信息中.....");
        Thread.sleep(400);
        return "查询商品成功!";
    }
}
