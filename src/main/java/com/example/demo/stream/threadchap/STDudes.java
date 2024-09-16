package com.example.demo.stream.threadchap;

public class STDudes {
    static volatile long flag = 0;

    synchronized void chat(long id) {
        if (flag == 0) {
            flag = id;
        }
        for (int x = 1; x < 3; x++) {
            System.out.print(Thread.currentThread().getName() + " " + x + " " + " flag =" + flag + " id =" + id);
            if (flag == id)
                System.out.print("yo ");
            else
                System.out.print("dude ");
            System.out.println();
        }
    }
}
