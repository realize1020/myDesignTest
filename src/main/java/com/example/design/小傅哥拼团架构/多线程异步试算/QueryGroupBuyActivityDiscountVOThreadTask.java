package com.example.design.小傅哥拼团架构.多线程异步试算;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
@Slf4j
public class QueryGroupBuyActivityDiscountVOThreadTask  implements Callable<java.lang.String> {

    @Override
    public String call() throws Exception {
        log.info("查询查询活动配置中.....");
        Thread.sleep(800);
        return "询查询活动成功!";
    }
}
