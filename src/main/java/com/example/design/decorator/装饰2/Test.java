package com.example.design.decorator.装饰2;

public class Test {
    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription()+"$"+beverage.cost());


        Beverage beverage2 =new MochaDecorator(beverage);
        System.out.println(beverage2.getDescription()+"$"+beverage2.cost());

        Beverage beverage3 =new SoyDecorator(beverage);
        System.out.println(beverage3.getDescription()+"$"+beverage3.cost());


        Beverage beverage4 =new MochaDecorator(new SoyDecorator(beverage));
        System.out.println(beverage4.getDescription()+"$"+beverage4.cost());

    }





}
