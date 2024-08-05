package com.example.design.decorator.装饰1;

public class DrinkImpl implements Drink{

    private String description;

    private int price = 1;


    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void bill(){
        System.out.println("账单为：购买了"+description+",价格为"+price);
    }
}
