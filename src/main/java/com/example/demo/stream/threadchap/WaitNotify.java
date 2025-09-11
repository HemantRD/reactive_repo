package com.example.demo.stream.threadchap;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WaitNotify {

    public static void main(String[] args) throws Exception {
//		SpringApplication.run(DemoApplication.class, args);
        Object sharedObject = new Object();

        Runnable task = () -> {
            synchronized (sharedObject) {
                System.out.println("I am doing background work, it will take 10 seconds to complete");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Done..notifying..");
                sharedObject.notify();
            }
        };

        Thread t1 = new Thread(task);
        t1.start();
        Thread.sleep(10);
        // i am main thread waiting for notification from t1 thread.
        synchronized (sharedObject) {
            System.out.println("Waiting");
            sharedObject.wait();
            System.out.println("Received notification...");
        }

    }

}
