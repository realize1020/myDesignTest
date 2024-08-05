package com.example.design.Aggregation;

public class Lamp {

    private Circle circle;

    private Bulb bulb;

    public Lamp(Circle circle, Bulb bulb) {
        this.circle = circle;
        this.bulb = bulb;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Bulb getBulb() {
        return bulb;
    }

    public void setBulb(Bulb bulb) {
        this.bulb = bulb;
    }


    public void print(){
        circle.print();
        bulb.print();
    }
}
