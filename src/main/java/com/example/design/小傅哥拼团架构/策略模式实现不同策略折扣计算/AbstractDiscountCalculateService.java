package com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;

import java.math.BigDecimal;

/**
 * 折扣计算服务抽象类
 */
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, ApplicationContext groupBuyDiscount) {
        // 1. 人群标签过滤
        if (DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            //判断人群，人群过滤 - 限定人群优惠
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if (!isCrowdRange) return originalPrice;
        }
        // 2. 折扣优惠计算
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    // 人群过滤 - 限定人群优惠
    private boolean filterTagId(String userId, String tagId) {
        // todo xiaofuge 后续开发这部分
        return true;
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, ApplicationContext groupBuyDiscount);
}
