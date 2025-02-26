package com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 满减优惠计算
 * @create 2024-12-22 12:12
 */
@Slf4j
@Service("N")
public class NCalculateService extends AbstractDiscountCalculateService {

    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, ApplicationContext groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式 - 直接为优惠后的金额
        //String marketExpr = groupBuyDiscount.getMarketExpr();
        // n元购
        return new BigDecimal(50);
    }

}
