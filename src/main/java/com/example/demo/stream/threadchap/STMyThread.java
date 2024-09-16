package com.example.demo.stream.threadchap;

public class STMyThread extends Thread {
    STMyThread() {
        System.out.println("MyThread ");
    }

    public void run() {
        System.out.println("bar");
    }

    public void run(String s) {
        System.out.println("baz ");
    }

    public static void main(String[] args) {
        Thread t = new STMyThread() {
            public void run() {
                System.out.println("foo ");
            }
        };
        t.start();
    }
}
