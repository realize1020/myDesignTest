package com.example.design.multiThread.线程等待与通知.base.wn.temp;



/**
 * 装弹
 */
public class Input implements  Runnable {
    private Bullet bullet;

    public Input(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            bullet.input();
            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
