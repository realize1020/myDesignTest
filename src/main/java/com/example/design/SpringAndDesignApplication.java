package com.example.design;

import com.example.design.source.springsource.扩展点ApplicaionListener.MyApplicationListener;
import com.example.design.strage.commonStrage.User;
import com.example.design.strage.springStrage.StrategyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringAndDesignApplication{

    public static void main(String[] args) {
        SpringApplication.run(SpringAndDesignApplication.class, args);
    }

}
