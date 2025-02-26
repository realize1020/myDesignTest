package com.example.design.小傅哥拼团架构;

import com.example.design.小傅哥拼团架构.多线程异步试算.*;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.DiscountTypeEnum;

import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        RequestAttributes marketProductEntity = new RequestAttributes();
        marketProductEntity.setUserId("xiaofuge");
        marketProductEntity.setSource("s01");
        marketProductEntity.setChannel("c01");
        marketProductEntity.setGoodsId("9890001");

        RootChain rootChain =new RootChain();
        DefaultActivityStrategyFactory defaultActivityStrategyFactory =new DefaultActivityStrategyFactory(rootChain);

        StrageHandler<String> stringStrageHandler = defaultActivityStrategyFactory.strageHandler();

        ApplicationContext context  = new ApplicationContext();
        //context.setMarketPlan("N"); //直接N元购,设置的是50元
        //context.setMarketPlan("MJ");//满减，满100减10
        //context.setMarketPlan("ZJ");//直减10元
        //context.setMarketPlan("ZK");//打折，打8折
        context.setMarketPlan("错误");
        context.setOriginPrice(new BigDecimal(100));//原价100
        context.setDiscountType(DiscountTypeEnum.BASE);
        try {
            String apply = stringStrageHandler.apply(marketProductEntity, context);
            System.out.println(apply);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
