package com.example.design.multiThread.双层循环直接跳出;

import java.awt.*;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        regry:
        while(true){
            System.out.println("外层循环");
            while (true){
                System.out.println("内层循环");
                while (true){
                    System.out.println("内层2循环");
                    break regry;
                }
            }
        }

        System.out.println("跳出外层循环");


        HashMap<String,String> map =new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");

        System.out.println(map.get("4"));





    }







}
