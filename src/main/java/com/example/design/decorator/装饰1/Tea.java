package com.example.design.decorator.装饰1;

public class Tea extends DrinkImpl{



    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public void setPrice(int price) {
        super.setPrice(price);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public int getPrice() {
        return super.getPrice();
    }
}
