package com.example.design.aop.localmessageAop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LocalTaskMessage {

    /**
     * 实体属性名称
     */
    String entityAttributeName() default "";

}
