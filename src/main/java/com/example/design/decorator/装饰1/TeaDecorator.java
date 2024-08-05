package com.example.design.decorator.装饰1;

public class TeaDecorator  extends DrinkImpl{

    private DrinkImpl drink;

    public TeaDecorator(DrinkImpl drink){
        this.drink = drink;
        this.drink.setDescription(drink.getDescription()+"加了茶水");
        this.drink.setPrice(this.drink.getPrice()+10);

    }

    @Override
    public String getDescription() {
        return drink.getDescription()+"加了茶水";
    }

    @Override
    public int getPrice() {
        return drink.getPrice()+10;
    }

    public void bill(){
        System.out.println("账单为：购买了"+drink.getDescription()+",价格为"+drink.getPrice());
    }
}
