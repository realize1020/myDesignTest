package com.example.design.小傅哥拼团架构.多线程异步试算;

import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.DiscountTypeEnum;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.IDiscountCalculateService;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ApplicationContext {

    //活动配置
    private String groupBuyActivityDiscountVO;

    //商品信息
    private String skuVo;

    //折扣类型
    private DiscountTypeEnum discountType;

    /**
     * 人群标签，特定优惠限定
     */
    private String tagId;

    /**
     * 营销优惠计划（ZJ:直减、MJ:满减、N元购）
     */
    private String marketPlan;

    /**
     * 原价
     */
    private BigDecimal originPrice;
    /**
     *
     */
    private BigDecimal deductionPrice;

    private Map<String, IDiscountCalculateService> discountCalculateServiceMap;
}
