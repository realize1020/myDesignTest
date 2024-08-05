package com.example.design.strage.commonStrage;


/**
 * 中级VIP
 */
public class IntermediateVIPStrategy implements PriceStrategy {
    @Override
    public double calculatePrice(double fee) {
        return fee * 0.8;
    }
}
