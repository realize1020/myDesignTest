// DemoApplication.java
package com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean;

import com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean.api.HelloApi;
import com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean.api.UserApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMyClients(basePackages = "com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean.api")
public class DemoApplication {
    @Resource
    private HelloApi helloApi;
    @Resource
    private UserApi userApi;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        DemoApplication app = ctx.getBean(DemoApplication.class);
        System.out.println(app.helloApi.sayHello("world"));
        System.out.println(app.userApi.getUser(123L));
    }
}