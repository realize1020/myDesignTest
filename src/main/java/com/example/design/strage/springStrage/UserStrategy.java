package com.example.design.strage.springStrage;


import org.springframework.stereotype.Component;

@Component
@UserTypeAnnotation(userType = 4)
//高级会员
public class UserStrategy implements PriceStrategy {
    public double calculatePrice(double cost) {
        return cost;
    }
}

