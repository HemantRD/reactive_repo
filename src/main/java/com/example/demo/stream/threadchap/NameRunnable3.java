
package com.example.demo.stream.threadchap;

public class NameRunnable3 implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            try {
                System.out.println(i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (i % 10 == 0) {
                System.out.println("String");
            }
        }
    }
}