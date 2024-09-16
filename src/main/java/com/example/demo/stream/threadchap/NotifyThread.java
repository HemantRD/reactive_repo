package com.example.demo.stream.threadchap;

public class NotifyThread implements Runnable {

    public int total;

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 1; i <= 100; i++) {
                total = i;
                try {
                    Thread.sleep(500l);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(i);
            }
            notify();
        }
    }


}
