package com.example.design.aop.localmessageAop;// Application.java
import com.example.design.aop.simple.TestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        TestLocalTaskMessageService service = context.getBean(TestLocalTaskMessageService.class);

        // 测试正常方法
        service.processOrder4(new TaskMessageEntityCommand("123", "订单1"));
        
        context.close();
    }
}
