package com.example.design.many;

public class InterfaceAImpl implements InterfaceA{
    @Override
    public void method1() {
        System.out.println("InterfaceA method1。。。。。。。。。。。。。。。。。。。");
    }

//    @Override
//    public void defaultMethod1() {
//        System.out.println("defaultMethodImpl1.................................");
//    }
//
//    @Override
//    public void defaultMethod2() {
//        System.out.println("defaultMethodImpl2.................................");
//    }

    public void InterfaceAImplMethod(){
        defaultMethod1();
        defaultMethod2();
    }
}
