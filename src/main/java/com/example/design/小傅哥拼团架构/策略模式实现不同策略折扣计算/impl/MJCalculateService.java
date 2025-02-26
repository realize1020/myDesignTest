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
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {

    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, ApplicationContext groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式 - 100,10 满100减10元
        //String marketExpr = groupBuyDiscount.getMarketExpr();
        String marketExpr = "100,10";
        String[] split = marketExpr.split(",");
        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal y = new BigDecimal(split[1].trim());

        // 不满足最低满减约束，则按照原价
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        // 折扣价格
        BigDecimal deductionPrice = originalPrice.subtract(y);

        // 判断折扣后金额，最低支付1分钱
        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return deductionPrice;
    }

}
