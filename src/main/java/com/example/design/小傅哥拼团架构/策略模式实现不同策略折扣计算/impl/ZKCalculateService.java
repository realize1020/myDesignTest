package com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl;

import com.example.design.小傅哥拼团架构.多线程异步试算.ApplicationContext;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 折扣优惠计算
 * @create 2024-12-22 12:12
 */
@Slf4j
@Service("ZK")
public class ZKCalculateService extends AbstractDiscountCalculateService {

    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice,ApplicationContext groupBuyDiscount) {
        log.info("优惠策略折扣计算");
        // 折扣价格8折
        BigDecimal deductionPrice = originalPrice.multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.HALF_UP);

        // 判断折扣后金额，最低支付1分钱
        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return deductionPrice;
    }

}
