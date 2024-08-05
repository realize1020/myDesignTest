package com.example.design.single;

public class SyschronizedSingleton {
    private static SyschronizedSingleton instance;

    private SyschronizedSingleton(){}

    public static synchronized SyschronizedSingleton getInstance() throws InterruptedException {
        if(instance==null){
            Thread.sleep(1000);
            instance = new SyschronizedSingleton();
        }
        return instance;
    }

    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(SyschronizedSingleton.getInstance());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

}
