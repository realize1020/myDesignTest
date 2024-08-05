package com.example.design.strage.springStrage;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;


@Component
public class StrategyFactory implements CommandLineRunner,ApplicationContextAware {


    private static volatile  ApplicationContext applicationContext;

    private static HashMap<Integer, PriceStrategy> userTypeStrategyMap;

    @Override
    public void run(String... args) throws Exception {
        initUserTypeStrategyMap();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static void initUserTypeStrategyMap() {
        Collection<PriceStrategy> userTypeStrategyList = applicationContext.getBeansOfType(PriceStrategy.class).values();

        userTypeStrategyMap = new HashMap<Integer, PriceStrategy>();

        for(PriceStrategy priceStrategy:userTypeStrategyList){
            Class<? extends PriceStrategy> aClass = priceStrategy.getClass();

            UserTypeAnnotation annotation = aClass.getAnnotation(UserTypeAnnotation.class);

            if (annotation != null) {
                userTypeStrategyMap.put(annotation.userType(), priceStrategy);
            }
        }


    }

    //通过这个方法获取对应的价格计算策略实现类
    public static PriceStrategy getPriceStrategyByUserType(Integer userType) {
        PriceStrategy strategy = userTypeStrategyMap.get(userType);
        if (strategy == null) {
            throw new RuntimeException("不支持的用户类型");
        }
        return strategy;
    }








}
