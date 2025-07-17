// DemoApplication.java
package com.example.design.source.springsource.注入带有特定注解的类集合;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyApplication {
    @Autowired
    private MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}