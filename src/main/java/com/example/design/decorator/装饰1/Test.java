package com.example.design.decorator.装饰1;

public class Test {
    public static void main(String[] args) {
        Tea tea =new Tea();
        tea.setDescription("茶水");
        tea.setPrice(10);
        tea.bill();


        Coffee coffee =new Coffee();
        coffee.setDescription("咖啡");
        coffee.setPrice(12);
        coffee.bill();


//        JuiceDecorator juiceDecorator  = new JuiceDecorator(tea);
//        juiceDecorator.bill();
//
//        //茶水里面加咖啡
//        CoffeeDecorator coffeeDecorator =new CoffeeDecorator(tea);
//        coffeeDecorator.bill();


        // 茶水里面加咖啡
        CoffeeDecorator2 coffeeDecorator =new CoffeeDecorator2(new JuiceDecorator2(tea));
        coffeeDecorator.bill();
    }
}
