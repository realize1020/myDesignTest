package com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;

import java.math.BigDecimal;

public interface IDiscountCalculateService {
    /**
     * 折扣计算
     *
     * @param userId           用户ID
     * @param originalPrice    商品原始价格
     * @param groupBuyDiscount 折扣计划配置
     * @return 商品优惠价格
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice, ApplicationContext groupBuyDiscount);
}
