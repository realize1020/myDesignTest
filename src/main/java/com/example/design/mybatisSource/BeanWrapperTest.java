package com.example.design.mybatisSource;

import com.example.design.mybatisSource.entity.User;
import lombok.val;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class BeanWrapperTest {
    public static void main(String[] args) {
        User user1=new User();
        user1.setId("123");
        user1.setUsername("wang");
        user1.setPassword("345");
        user1.setHas(true);

//        BeanWrapper beanWrapper =new BeanWrapper(null,user1);
//        System.out.println(beanWrapper.getGetterNames());


        Map<String,Object> map =new HashMap<String,Object>();
        map.put("id","123");
        map.put("user",user1);
        map.put("userList",new ArrayList<User>());

        ObjectWrapperFactory objectWrapperFactory =new DefaultObjectWrapperFactory();
        ObjectFactory objectFactory =new DefaultObjectFactory();
        ReflectorFactory reflectorFactory =new DefaultReflectorFactory();
        MetaObject metaObject = MetaObject.forObject(user1, objectFactory, objectWrapperFactory, reflectorFactory);
        //ObjectWrapper wrapperFor = objectWrapperFactory.getWrapperFor(metaObject, user1);
//        System.out.println(wrapperFor.getGetterNames());
        //BeanWrapper beanWrapper =new BeanWrapper(metaObject,user1);
        //val username = beanWrapper.getGetterType("username");
//        Class<?> id = beanWrapper.getGetterType("id");
//        Class<?> password = beanWrapper.getGetterType("password");
//        System.out.println("username类型："+username+", id类型="+id+", password类型="+password);
//        System.out.println(beanWrapper.hasGetter("username"));
        BeanWrapper beanWrapper =new BeanWrapper(metaObject,user1);
//        System.out.println(beanWrapper.getGetterType("id"));
//        System.out.println(beanWrapper.getGetterType("user"));
        String[] getterNames = beanWrapper.getGetterNames();

        System.out.println(Arrays.toString(getterNames));
        System.out.println(Arrays.toString(beanWrapper.getSetterNames()));

        System.out.println(beanWrapper.findProperty("username", true));



    }
}
