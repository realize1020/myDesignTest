package com.example.design.小傅哥拼团架构.多线程异步试算;


import lombok.Getter;
import lombok.Setter;

public abstract class AbstractStrategyChain<R> implements StrageHandler<R>,StrategyMapper<R>{

    @Setter
    @Getter
    StrageHandler<R> defaultStrageHandler = StrageHandler.defaultStrageHandler;

    public R execute(RequestAttributes attributes, ApplicationContext context) throws Exception {
        StrageHandler<R> strageHandler = get(attributes, context);

        if(strageHandler!=null){
            return strageHandler.apply(attributes,context);
        }
        return defaultStrageHandler.apply(attributes,context);

    }


    @Override
    public R apply(RequestAttributes attributes, ApplicationContext context) throws  Exception {
        // 异步加载数据
        multiThread(attributes, context);
        // 业务流程受理
        return doApply(attributes, context);
    }

    /**
     * 异步加载数据
     */
    protected abstract void multiThread(RequestAttributes attributes, ApplicationContext context) throws Exception;

    /**
     * 业务流程受理
     */
    protected abstract R doApply(RequestAttributes attributes, ApplicationContext context) throws Exception;

}
