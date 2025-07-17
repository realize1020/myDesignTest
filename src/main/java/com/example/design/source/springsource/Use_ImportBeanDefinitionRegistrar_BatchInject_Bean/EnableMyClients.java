// EnableMyClients.java
package com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MyClientRegistrar.class)
public @interface EnableMyClients {
    String[] basePackages();
}