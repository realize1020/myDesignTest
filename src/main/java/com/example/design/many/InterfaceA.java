package com.example.design.many;

public interface InterfaceA {

    String name="InterfaceA";

    void method1();

    default void defaultMethod1(){
        staticMethod1();
        System.out.println(name+" defaultMethod1.................................");
    }

    default void defaultMethod2(){
        staticMethod1();
        System.out.println(name+" defaultMethod2.................................");
    }

    static void staticMethod1(){
        System.out.println(name+" staticMethod1.................................");
    }
}
