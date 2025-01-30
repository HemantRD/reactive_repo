package com.example.demo.stream.threadchap;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class NotifyAllCalc2 extends Thread {
    public int total;
    Condition condition;
    Lock lock;

    public NotifyAllCalc2(Condition condition, Lock lock) {
        this.condition = condition;
        this.lock = lock;
    }

    public void run() {
        lock.lock();
        try {
//            synchronized (this) {
            for (int i = 0; i <= 100; i++) {
                total = i;
                System.out.print(i + ",");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            notifyAll();
            condition.signalAll();
            System.out.println("total is :" + total);
//            }
        } finally {
            lock.unlock();
        }
    }
}