package com.example.design.strage.springStrage;


import org.springframework.stereotype.Component;

@Component
@UserTypeAnnotation(userType = 3)
//初级会员
public class InitialVIPStrategy implements PriceStrategy {
    public double calculatePrice(double cost) {
        return cost * 0.9;
    }
}

