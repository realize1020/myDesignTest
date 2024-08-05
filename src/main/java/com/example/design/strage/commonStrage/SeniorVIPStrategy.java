package com.example.design.strage.commonStrage;


/**
 * 高级VIP
 */
public class SeniorVIPStrategy implements PriceStrategy {
    @Override
    public double calculatePrice(double fee) {
        return fee * 0.7;
    }



}
