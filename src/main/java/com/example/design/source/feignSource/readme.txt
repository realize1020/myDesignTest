1. 结构说明
接口：定义远程服务方法（如 UserClient）
MethodHandler：每个方法的处理器（类似 SynchronousMethodHandler）
FeignInvocationHandler：JDK 动态代理的 InvocationHandler
Feign：负责生成代理对象
Client：模拟底层 HTTP 客户端