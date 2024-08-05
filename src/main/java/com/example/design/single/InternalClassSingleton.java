package com.example.design.single;

public class InternalClassSingleton {



    private static class InstanceHolder{
        private static InternalClassSingleton instance = new InternalClassSingleton();
    }

    private InternalClassSingleton(){

    }

    public InternalClassSingleton getInstance(){
        return InstanceHolder.instance;
    }

}
