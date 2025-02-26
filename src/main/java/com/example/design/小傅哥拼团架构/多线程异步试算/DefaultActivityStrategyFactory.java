package com.example.design.小傅哥拼团架构.多线程异步试算;

/**
 * @description 活动策略工厂
 */
public class DefaultActivityStrategyFactory {
    private final RootChain rootChain;

    public DefaultActivityStrategyFactory(RootChain rootChain){
        this.rootChain=rootChain;
    }

    public StrageHandler<String> strageHandler(){
        return rootChain;
    }

}
