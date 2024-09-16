package com.example.demo.stream.threadchap;

public class NotifyAllReaders implements Runnable {

    public NotifyAllCalc calculator;

    public NotifyAllReaders(NotifyAllCalc calculator) {
        this.calculator = calculator;
    }

    @Override
    public void run() {
        synchronized (calculator) {
            try {
                System.out.println("Waiting for NotifyAllThreads to complete -> " + Thread.currentThread().getName());
                calculator.wait();
                System.out.println("Yes...Got the result : " + calculator.total + " My name is " + Thread.currentThread().getName());
//                System.out.println("Yes...Got the result : TBD My name is " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
