package com.example.demo.stream.threadchap;

public class InSync extends Thread {
    StringBuffer letter;

    public InSync(StringBuffer letter) {
        this.letter = letter;
    }

    @Override
    public void run() {
        synchronized (letter) {
            for (int i = 1; i <= 100; i++)
                System.out.print(letter);
            System.out.println();
            char temp = letter.charAt(0);
            ++temp;
            letter.setCharAt(0, temp);
        }
    }
}
