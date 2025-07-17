package com.example.design.source.mybatisSource;

import com.example.design.source.mybatisSource.entity.User;
import org.apache.ibatis.reflection.property.PropertyCopier;

public class TestPropertyCopier {
    public static void main(String[] args) {
        //常规属性设置
        User user1=new User();
        user1.setId("123");
        user1.setUsername("wang");
        user1.setPassword("345");
        user1.setHas(true);

        User user2=new User();
//        user2.setId(user1.getId());
//        user2.setUsername(user1.getUsername());
//        user2.setPassword(user1.getPassword());


        //利用Mybatis源码当中的PropertyCopier类
        PropertyCopier.copyBeanProperties(user1.getClass(),user1,user2);

        System.out.println(user2.toString());
    }
}
