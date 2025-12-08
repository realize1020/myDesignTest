package com.example.design.aop.localmessageAop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务消息实体命令
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/11/10 08:19
 */
@Data
public class TaskMessageEntityCommand {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;


    public TaskMessageEntityCommand() {
    }

    public TaskMessageEntityCommand(String taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }


}
