package com.example.design.strage.springStrage;


import org.springframework.stereotype.Component;

@Component
@UserTypeAnnotation(userType = 1)
//高级会员
public class SeniorVIPStrategy implements PriceStrategy {
    public double calculatePrice(double cost) {
        return cost * 0.7;
    }
}

