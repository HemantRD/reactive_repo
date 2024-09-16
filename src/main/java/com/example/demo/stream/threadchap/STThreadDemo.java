package com.example.demo.stream.threadchap;

public class STThreadDemo extends Thread {
    synchronized void a() {
        actBusy();
    }

    static synchronized void b() {
        actBusy();
    }

    static void actBusy() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        final STThreadDemo x = new STThreadDemo();
        final STThreadDemo y = new STThreadDemo();
        Runnable runnable = () -> {
            int option = (int) Math.random() * 4;
            switch (option) {
                case 0:
                    x.a();
                    break;
                case 1:
                    x.b();
                    break;
                case 2:
                    y.a();
                    break;
                case 4:
                    y.b();
                    break;
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }
}
