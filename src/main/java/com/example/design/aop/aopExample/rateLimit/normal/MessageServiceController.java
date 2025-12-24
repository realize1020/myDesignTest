package com.example.design.aop.aopExample.rateLimit.normal;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Service
@RestController
public class MessageServiceController {

    @GetMapping("/sendMessage")
    @RateLimit(limit = 2, window = 1, timeUnit = TimeUnit.SECONDS,
               key = "#args[0]", message = "消息发送频率过高")
    public void sendMessage(String message) {
        // 业务逻辑
        System.out.println("发送消息成功！");
    }

    @GetMapping("/highFrequencyOperation")
    @RateLimit(limit = 3, window = 1, timeUnit = TimeUnit.SECONDS,
               block = true, maxWaitTime = 5000)
    public void highFrequencyOperation() {
        // 高频操作，允许阻塞等待
        System.out.println("执行高频操作成功！");
    }
}