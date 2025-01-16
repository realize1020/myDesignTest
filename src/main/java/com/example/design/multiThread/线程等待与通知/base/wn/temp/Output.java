package com.example.design.multiThread.线程等待与通知.base.wn.temp;



/**
 * 射击
 */
public class Output implements Runnable {
    private Bullet bullet;

    public Output(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            bullet.output();
            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

