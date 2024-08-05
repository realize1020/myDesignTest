package com.example.design.内部类;


import lombok.Data;

@Data
public class OuterClass {

    private   String username;

    private  String password;

    private void print(){
        this.toString();
    }


   class InnterClass{
        //获取外部密码加密
       void list(){
           password=password.trim();
       }


    }

}
