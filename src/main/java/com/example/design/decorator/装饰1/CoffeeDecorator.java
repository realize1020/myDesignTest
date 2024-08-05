package com.example.design.decorator.装饰1;

public class CoffeeDecorator  extends DrinkImpl{
    private DrinkImpl drink;

    public CoffeeDecorator(DrinkImpl drink){
        this.drink = drink;
        this.drink.setDescription(drink.getDescription()+"加了咖啡");
        this.drink.setPrice(drink.getPrice()+12);
    }

    @Override
    public String getDescription() {

        return drink.getDescription()+"加了咖啡";
    }

    @Override
    public int getPrice() {

        return drink.getPrice()+12;
    }

    public void bill(){
        System.out.println("账单为：购买了"+drink.getDescription()+",价格为"+drink.getPrice());
    }
}
