package com.example.design.test;

import java.io.Serializable;
import java.util.ArrayList;

public class SomeClass {
    public static void main(String[] args) {
        // 匿名内部类的静态方法中获取当前类名
//        new Object() {
//            public final void someStaticMethod() {
//                Class<?> enclosingClass = getClass().getEnclosingClass();
//                System.out.println("Enclosing class name: " + enclosingClass.getName());
//            }
//        }.someStaticMethod();

        new SomeClassListener(){

            @Override
            public void OnEvent() {
                String outName = getClass().getEnclosingClass().getName();
                String name = getClass().getName();
                System.out.println("Enclosing class name: " + outName);
                System.out.println("Current class name: " + name);

            }
        }.OnEvent();


        ArrayList<Integer> array = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }};
        System.out.println(array.toString());


    }
}

interface SomeClassListener extends Serializable {
    void OnEvent();

}
