package com.example.demo.stream.threadchap;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NotifyAllReaders2 implements Runnable {

    public NotifyAllCalc2 calculator;
    Condition condition;
    Lock lock;

    public NotifyAllReaders2(NotifyAllCalc2 calculator, Condition condition, Lock lock) {
        this.calculator = calculator;
        this.condition = condition;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
//            synchronized (calculator) {
            try {
                System.out.println("Waiting for NotifyAllThreads to complete -> " + Thread.currentThread().getName());
//                calculator.wait();
                condition.await();
                System.out.println("Yes...Got the result : " + calculator.total + " My name is " + Thread.currentThread().getName());
//                System.out.println("Yes...Got the result : TBD My name is " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            }
        } finally {
            lock.unlock();
        }
    }


}
