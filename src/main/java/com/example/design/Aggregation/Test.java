package com.example.design.Aggregation;

public class Test {
    public static void main(String[] args) {
        Circle circle = new Circle();

        Bulb bigBulb = new BigBulb();

        Lamp lamp =new Lamp(circle,bigBulb);



        lamp.setCircle(circle);

        lamp.setBulb(bigBulb);

        lamp.print();

    }
}
