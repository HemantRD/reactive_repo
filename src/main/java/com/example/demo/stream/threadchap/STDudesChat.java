package com.example.demo.stream.threadchap;

public class STDudesChat implements Runnable {
    static STDudes d;

    public static void main(String args[]) {
        new STDudesChat().go();
    }

    void go() {
        d = new STDudes();
        new Thread(new STDudesChat()).start();
        new Thread(new STDudesChat()).start();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        d.chat(Thread.currentThread().getId());
    }

}
