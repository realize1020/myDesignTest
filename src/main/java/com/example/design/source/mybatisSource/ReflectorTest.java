package com.example.design.source.mybatisSource;

import com.example.design.source.mybatisSource.entity.User;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ReflectorTest {
    public static void main(String[] args) {
        User user=new User();
        user.setUsername("11111");
        Reflector reflector =new Reflector(user.getClass());
        System.out.println(Arrays.toString(reflector.getSetablePropertyNames()));
        Class<?> username = reflector.getGetterType("username");
        System.out.println(username);
        Class<? extends Reflector> aClass = reflector.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            try {
                if(field.getName().equals("getMethods")){
                    HashMap<String, Invoker> map = (HashMap<String, Invoker>)field.get(reflector);
                    Collection<Invoker> values = map.values();
                    for(Invoker invoker:values){
                        try {
                            System.out.println(invoker.invoke(user, null));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //System.out.println(field.getName());
        }

    }
}
