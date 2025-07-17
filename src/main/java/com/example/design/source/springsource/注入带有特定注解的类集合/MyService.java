package com.example.design.source.springsource.注入带有特定注解的类集合;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class MyService {

    @MyLoadBalanced
    //@Qualifier //直接用这个@Qualifier注解也行，不使用@MyLoadBalanced多包一层
    @Autowired
    private List<MyRestTemplate> myRestTemplates;

    @PostConstruct
    public void init() {
        for (MyRestTemplate myRestTemplate : myRestTemplates) {
            System.out.println("初始化MyRestTemplate: "+myRestTemplate.toString());
        }
    }

    @Bean
    public SmartInitializingSingleton init2(){
        return ()->{
            for (MyRestTemplate myRestTemplate : myRestTemplates) {
                System.out.println("SmartInitializingSingleton初始化MyRestTemplate: "+myRestTemplate.toString());
            }
        };
    }
}
