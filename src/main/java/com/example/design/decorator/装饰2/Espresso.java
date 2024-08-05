package com.example.design.decorator.装饰2;

public class Espresso extends Beverage{

    public Espresso(){
        description = "Espresso";
    }


    @Override
    public double cost() {
        return 1.99;
    }
}
