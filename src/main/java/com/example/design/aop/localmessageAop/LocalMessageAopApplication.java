package com.example.design.aop.localmessageAop;// Application.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LocalMessageAopApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LocalMessageAopApplication.class, args);

        TestLocalTaskMessageService service = context.getBean(TestLocalTaskMessageService.class);

        // 测试正常方法
        service.processOrder4(new TaskMessageEntityCommand("123", "订单1"));
        
        context.close();
    }
}
