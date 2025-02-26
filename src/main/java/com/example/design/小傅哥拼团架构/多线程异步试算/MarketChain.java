package com.example.design.小傅哥拼团架构.多线程异步试算;

import com.alibaba.fastjson.JSON;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.IDiscountCalculateService;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl.MJCalculateService;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl.NCalculateService;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl.ZJCalculateService;
import com.example.design.小傅哥拼团架构.策略模式实现不同策略折扣计算.impl.ZKCalculateService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class MarketChain<String> extends AbstractStrategyChain<String>{

    private EndChain endChain =new EndChain();

    private ErrorChain errorChain =new ErrorChain();

    private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);

    //@Resource 如果是spring的话，可以直接注入进来，现在用静态代码块把这些实现类put进来
    private Map<java.lang.String, IDiscountCalculateService> discountCalculateServiceMap;

    {
        discountCalculateServiceMap=new HashMap<>();
        discountCalculateServiceMap.put("MJ",new MJCalculateService());
        discountCalculateServiceMap.put("N",new NCalculateService());
        discountCalculateServiceMap.put("ZJ",new ZJCalculateService());
        discountCalculateServiceMap.put("ZK",new ZKCalculateService());
    }

    @Override
    protected void multiThread(RequestAttributes attributes, ApplicationContext context) throws ExecutionException, InterruptedException {
        log.info("MarketChain的multiThread...........");
        // 异步查询活动配置
        log.info("等待异步查询活动配置....");
        QueryGroupBuyActivityDiscountVOThreadTask queryGroupBuyActivityDiscountVOThreadTask = new QueryGroupBuyActivityDiscountVOThreadTask();
        FutureTask<java.lang.String>  groupBuyActivityDiscountVOFutureTask =new FutureTask<>(queryGroupBuyActivityDiscountVOThreadTask);
        threadPoolExecutor.execute(groupBuyActivityDiscountVOFutureTask);

        // 异步查询商品信息 - 在实际生产中，商品有同步库或者调用接口查询。这里暂时使用DB方式查询。
        QuerySkuVOFromDBThreadTask querySkuVOFromDBThreadTask = new QuerySkuVOFromDBThreadTask();
        log.info("等待异步查询商品信息....");
        FutureTask<java.lang.String> skuVOFutureTask = new FutureTask<>(querySkuVOFromDBThreadTask);
        threadPoolExecutor.execute(skuVOFutureTask);

        context.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOFutureTask.get());
        context.setDiscountCalculateServiceMap(discountCalculateServiceMap);
        log.info("异步查询活动配置完毕！！！");
        context.setSkuVo(skuVOFutureTask.get());
        log.info("异步查询商品信息完毕！！！");

        log.info("拼团商品查询试算服务-MarketNode userId:{} 异步线程加载数据「GroupBuyActivityDiscountVO、SkuVO」完成");
    }

    @Override
    protected String doApply(RequestAttributes attributes, ApplicationContext context) throws Exception {
        log.info("拼团商品查询试算服务");


        IDiscountCalculateService discountCalculateService = discountCalculateServiceMap.get(context.getMarketPlan());
        if (null == discountCalculateService) {
            return execute(attributes,context);
        }

        // 折扣价格
        BigDecimal deductionPrice = discountCalculateService.calculate(attributes.getUserId(), context.getOriginPrice(), context);
        context.setDeductionPrice(deductionPrice);

        return execute(attributes, context);
    }

    @Override
    public StrageHandler get(RequestAttributes attributes, ApplicationContext context) {
        IDiscountCalculateService discountCalculateService = context.getDiscountCalculateServiceMap().get(context.getMarketPlan());
        if (null == discountCalculateService){
            return errorChain;
        }
        return endChain;
    }
}
