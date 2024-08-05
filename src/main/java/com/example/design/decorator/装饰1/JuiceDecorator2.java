package com.example.design.decorator.装饰1;

public class JuiceDecorator2 extends DrinkImpl{

    private DrinkImpl drink;

    private String description;

    private int price;

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    public JuiceDecorator2(DrinkImpl drink){
        this.drink = drink;
        this.setDescription(drink.getDescription()+"加了果汁");
        this.setPrice(drink.getPrice()+8);

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

        System.out.println("账单为：购买了"+this.getDescription()+",价格为"+this.getPrice());
    }
}
