package com.example.design.decorator.装饰1;

public class JuiceDecorator extends DrinkImpl{

    private DrinkImpl drink;

    public JuiceDecorator(DrinkImpl drink){
        this.drink = drink;
        this.drink.setDescription(drink.getDescription()+"加了果汁");
        this.drink.setPrice(this.drink.getPrice()+8);

    }

    @Override
    public String getDescription() {
        return drink.getDescription()+"加了果汁";
    }

    @Override
    public int getPrice() {
        return drink.getPrice()+8;
    }

    public void bill(){

        System.out.println("账单为：购买了"+drink.getDescription()+",价格为"+drink.getPrice());
    }
}
