package com.example.design.decorator.装饰2;

public class MochaDecorator extends CondimentDecorator{

    Beverage beverage;

    public MochaDecorator(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+",Mocha";
    }

    @Override
    public double cost() {
        return beverage.cost()+0.2;
    }
}
