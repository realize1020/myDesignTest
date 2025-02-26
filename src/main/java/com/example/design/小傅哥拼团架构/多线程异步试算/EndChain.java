package com.example.design.小傅哥拼团架构.多线程异步试算;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EndChain extends AbstractStrategyChain<String>{


    @Override
    protected void multiThread(RequestAttributes attributes, ApplicationContext context) throws Exception {
        log.info("EndChain的multiThread.....");
    }

    @Override
    protected String doApply(RequestAttributes attributes, ApplicationContext context) throws Exception {
        log.info("拼团商品查询试算服务-EndNode");
        String groupBuyActivityDiscountVO = context.getGroupBuyActivityDiscountVO();
        String skuVo = context.getSkuVo();

        return groupBuyActivityDiscountVO+"\n"+skuVo+"\n"+"试算结束，价格为"+context.getDeductionPrice()+"元";
    }

    @Override
    public StrageHandler get(RequestAttributes attributes, ApplicationContext context) {
        return defaultStrageHandler;
    }
}
