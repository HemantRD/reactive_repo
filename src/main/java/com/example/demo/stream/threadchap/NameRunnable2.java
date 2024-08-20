
package com.example.demo.stream.threadchap;

public class NameRunnable2 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i <= 4; i++) {
            System.out.println("Run By "+ Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}