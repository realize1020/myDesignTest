package com.example.design.single;

/**
 * 线程不安全
 */
public class LazySingleton {

    private static LazySingleton instance;

    private LazySingleton(){}

    public static LazySingleton getInstance() throws InterruptedException {
        if(instance==null){
            Thread.sleep(1000);
            instance = new LazySingleton();
        }
        return instance;
    }


    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(LazySingleton.getInstance());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
