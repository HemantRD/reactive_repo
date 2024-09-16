package com.example.demo.stream.threadchap;

class NotifyAllCalc extends Thread {
    public int total;

    public NotifyAllCalc() {
    }

    public void run() {
        synchronized (this) {
            for (int i = 0; i <= 100; i++) {
                total = i;
                System.out.print(i + ",");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            notifyAll();
            System.out.println("total is :" + total);
        }
    }
}