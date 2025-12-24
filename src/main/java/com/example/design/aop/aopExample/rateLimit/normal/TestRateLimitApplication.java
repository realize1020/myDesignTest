package com.example.design.aop.aopExample.rateLimit.normal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
public class TestRateLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestRateLimitApplication.class, args);
    }

}
