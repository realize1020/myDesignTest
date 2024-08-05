package com.example.design.strage.commonStrage;

/**
 * 初级VIP
 */
public class InitialVIPStrategy implements PriceStrategy {
    @Override
    public double calculatePrice(double fee) {
        return fee * 0.9;
    }
}