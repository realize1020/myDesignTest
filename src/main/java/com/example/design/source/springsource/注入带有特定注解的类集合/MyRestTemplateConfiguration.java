package com.example.design.source.springsource.注入带有特定注解的类集合;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRestTemplateConfiguration {


    @MyLoadBalanced
    //@Qualifier//直接用这个@Qualifier注解也行，不使用@MyLoadBalanced多包一层
    @Bean
    public MyRestTemplate myRestTemplate() {
        return new MyRestTemplate("myRestTemplate");
    }


    @MyLoadBalanced
    //@Qualifier//直接用这个@Qualifier注解也行，不使用@MyLoadBalanced多包一层
    @Bean
    public MyRestTemplate orderServiceRestTemplate() {
        return new MyRestTemplate("orderServiceRestTemplate");
    }


    @MyLoadBalanced
    @Bean
    public MyRestTemplate userServiceRestTemplate() {
        return new MyRestTemplate("userServiceRestTemplate");
    }

    @Bean
    public MyRestTemplate loginServiceRestTemplate() {
        return new MyRestTemplate("loginServiceRestTemplate");
    }
}
