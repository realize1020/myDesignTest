package com.example.design.decorator.装饰2;

public class SoyDecorator extends CondimentDecorator{

    Beverage beverage;

    public SoyDecorator(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+",Soy";
    }

    @Override
    public double cost() {
        return beverage.cost()+0.3;
    }
}
