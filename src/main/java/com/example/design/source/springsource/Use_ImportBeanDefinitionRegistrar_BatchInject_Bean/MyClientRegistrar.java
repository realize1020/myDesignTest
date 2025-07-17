// MyClientRegistrar.java
package com.example.design.source.springsource.Use_ImportBeanDefinitionRegistrar_BatchInject_Bean;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.Map;
import java.util.Set;

public class MyClientRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 1. 读取注解参数
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableMyClients.class.getName());
        String[] basePackages = (String[]) attrs.get("basePackages");

        // 2. 扫描包下所有接口
        //ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        ClassPathScanningCandidateComponentProvider scanner  = getScanner();
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getClassMetadata().isInterface());

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);//这个方法里的scanCandidateComponents方法->isCandidateComponent(sbd)方法是默认过滤方法。过滤逻辑可能和我们想要的逻辑有冲突，所以使用getScanner()重写了isCandidateComponent(sbd)方法
            for (BeanDefinition candidate : candidates) {
                String beanClassName = candidate.getBeanClassName();
                // 3. 注册为FactoryBean（这里用代理模拟）
                BeanDefinitionBuilder builder = BeanDefinitionBuilder
                        .genericBeanDefinition(MyClientFactoryBean.class);
                builder.addConstructorArgValue(beanClassName);
                registry.registerBeanDefinition(beanClassName, builder.getBeanDefinition());
            }
        }
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }
}