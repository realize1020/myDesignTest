package com.example.design.single;

public class HungrySingleton {

    private final static HungrySingleton instance =new HungrySingleton();

    private HungrySingleton(){

    }

    public static HungrySingleton getInstance() throws InterruptedException {
        Thread.sleep(1000);
        return instance;
    }

    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(HungrySingleton.getInstance());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }


}
