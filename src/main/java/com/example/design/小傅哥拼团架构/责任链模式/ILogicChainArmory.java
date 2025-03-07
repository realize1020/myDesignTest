package com.example.design.小傅哥拼团架构.责任链模式;

public interface ILogicChainArmory {

    ILogicLink next();

    ILogicLink appendNext(ILogicLink next);
}
