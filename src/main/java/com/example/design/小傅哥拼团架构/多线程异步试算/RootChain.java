package com.example.design.小傅哥拼团架构.多线程异步试算;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RootChain extends AbstractStrategyChain<String>{

    private MarketChain marketChain =new MarketChain();


    @Override
    protected void multiThread(RequestAttributes attributes, ApplicationContext context) throws Exception {
        log.info("RootChain的multiThread...........");
    }

    @Override
    protected String doApply(RequestAttributes attributes, ApplicationContext context) throws Exception {
        log.info("拼团商品查询试算服务-RootChain");

        return execute(attributes,context);
    }

    @Override
    public StrageHandler<String> get(RequestAttributes attributes, ApplicationContext context) {
        return marketChain;
    }
}
