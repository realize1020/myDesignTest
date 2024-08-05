package com.example.design.single;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceSingleton {

    private static final AtomicReference<AtomicReferenceSingleton> INSTANCE =new AtomicReference<AtomicReferenceSingleton>();

    private static AtomicReferenceSingleton instance;


    private AtomicReferenceSingleton(){}

    public static AtomicReferenceSingleton getInstance() throws InterruptedException {
        for(;;){
            instance = INSTANCE.get();
            if(instance!=null){
                Thread.sleep(1000);
                return instance;
            }
            INSTANCE.compareAndSet(null,new AtomicReferenceSingleton());
            return INSTANCE.get();
        }
    }


    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(AtomicReferenceSingleton.getInstance());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

}
