package com.example.design.strage.commonStrage;

/**
 * 普通用户
 */
public class UserStrategy implements PriceStrategy {
    @Override
    public double calculatePrice(double fee) {
        return fee;
    }
}