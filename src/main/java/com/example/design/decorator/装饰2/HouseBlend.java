package com.example.design.decorator.装饰2;

public class HouseBlend extends Beverage{

    public HouseBlend(){
        description = "House Blend Coffee";
    }


    @Override
    public double cost() {
        return 0.98;
    }
}
