## 关键知识点总结
1. 无参构造器 ： clazz.getDeclaredConstructor().newInstance()
2. 有参构造器 ：需要指定参数类型和值
3. 私有构造器 ：需要 setAccessible(true) 绕过访问限制
4. 静态工厂方法 ：使用 Method.invoke(null, args) 调用
5. 构造器选择 ：类似Spring的 createBeanInstance 方法逻辑
6. 属性设置 ：通过反射Field设置字段值
7. 异常处理 ：包装具体异常为统一的业务异常
   这个示例完整展示了Java反射实例化的各种场景，帮助您深入理解Spring框架中实例化策略的实现原理。