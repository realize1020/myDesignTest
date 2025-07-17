package com.example.design.run;

import com.example.design.source.nacosSource.springcloud监听nacos配置变化并刷新.MyNacosContextRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages = "com.example.design")
public class SpringNaocsConfigApplication implements CommandLineRunner {
    static ConfigurableApplicationContext context =null;
    public static void main(String[] args) {
        //*********************nacos配置中心测试**************************
        SpringApplication.run(SpringNaocsConfigApplication.class, args);
    }
    @Autowired
    private MyNacosContextRefresher refresher;

    @Override
    public void run(String... args) throws Exception {
       //MyNacosContextRefresher refresher = context.getBean(MyNacosContextRefresher.class);
        // 模拟配置变更
//        Listener listener = refresher.listenerMap.get("defautGroup:12345");
//        if (listener != null) {
//            listener.receiveConfigInfo("12345");
//        }
        System.out.println("CommandLineRunner。。。。。。。。");
    }
}
