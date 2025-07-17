package com.example.design.source.springsource.注入带有特定注解的类集合;

import lombok.Data;

public class MyRestTemplate {
    private String name;

    public MyRestTemplate(){}

    public MyRestTemplate(String name){
        this.name = name;
        System.out.println(name+"被初始化了.....................");
    }
}
