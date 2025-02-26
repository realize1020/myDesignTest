package com.example.design.小傅哥拼团架构.多线程异步试算;

import com.alibaba.fastjson.JSON;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.IDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ErrorChain extends AbstractStrategyChain<String> {
    @Override
    protected void multiThread(RequestAttributes attributes, ApplicationContext context) throws Exception {

    }

    @Override
    protected String doApply(RequestAttributes attributes, ApplicationContext context) throws Exception {

        Map<java.lang.String, IDiscountCalculateService> discountCalculateServiceMap = context.getDiscountCalculateServiceMap();
        IDiscountCalculateService discountCalculateService = discountCalculateServiceMap.get(context.getMarketPlan());
        if (null == discountCalculateService){
            log.info("不存在{}类型的折扣计算服务，支持类型为:{}", context.getMarketPlan(), JSON.toJSONString(discountCalculateServiceMap.keySet()));
            throw new Exception("不存在的类型的折扣计算服务");
        }
        return "服务出错！";

    }

    @Override
    public StrageHandler<String> get(RequestAttributes attributes, ApplicationContext context) {
        return defaultStrageHandler;
    }
}
