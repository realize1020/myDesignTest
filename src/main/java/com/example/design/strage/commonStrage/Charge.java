package com.example.design.strage.commonStrage;


/**
 * 费用类
 */
public class Charge {
    private PriceStrategy strategy;

    public Charge(PriceStrategy strategy) {
        this.strategy = strategy;
    }

    public double price(double fee) {
        return strategy.calculatePrice(fee);
    }



}
