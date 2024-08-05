package com.example.design.strage.springStrage;


import org.springframework.stereotype.Component;

@Component
@UserTypeAnnotation(userType = 2)
//中级会员
public class IntermediateVIPStrategy implements PriceStrategy {
    public double calculatePrice(double cost) {
        return cost * 0.8;
    }
}

