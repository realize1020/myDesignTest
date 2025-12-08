package com.example.design.aop.localmessageAop;

import org.springframework.stereotype.Service;

@Service
public class TestLocalTaskMessageService {

    /**
     * 测试第一种情况：不设置 entityAttributeName
     * @param command
     */
    @LocalTaskMessage
    public void processOrder(TaskMessageEntityCommand command) {
        System.out.println("处理订单：" + command.getTaskName());
    }


    /**
     * 测试第二种情况：设置 entityAttributeName
     * @param command
     */
    @LocalTaskMessage(entityAttributeName = "command")
    public void processOrder2(TaskMessageEntityCommand command) {
        System.out.println("处理订单：" + command.getTaskName());
    }


    /**
     * 测试第三种情况，使用静态方法，静态方法不会被AOP拦截
     * @param command
     */
    @LocalTaskMessage(entityAttributeName = "command")
    public static void processOrder3(TaskMessageEntityCommand command) {
        System.out.println("处理订单：" + command.getTaskName());
    }
    /**
     * 测试第四种情况
     * @param command
     */
    @LocalTaskMessage(entityAttributeName = "request.command")
    public void processOrder4(TaskMessageEntityCommand command) {
        System.out.println("处理订单：" + command.getTaskName());
    }

}
