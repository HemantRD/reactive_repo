package com.example.demo.stream.threadchap;

public class STChess implements Runnable {

    @Override
    public void run() {
        move(Thread.currentThread().getId());
    }

    synchronized void move(long id) {
        System.out.print(id + " ");
        System.out.print(id + " ");
    }

    public static void main(String[] args) {
        STChess ch = new STChess();
        new Thread(ch).start();
        new Thread(new STChess()).start();
    }
}
