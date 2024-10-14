package com.example.demo.stream.concurrencychap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Concurrency {

    public static void main(String[] args) throws Exception {


    }

    static class ArrayListRunnable implements Runnable {
        private List<Integer> list = new ArrayList<>();

        public ArrayListRunnable() {
            for (int i = 0; i < 100000; i++) {
                list.add(i);
            }
        }

        public void run() {
            String tName = Thread.currentThread().getName();
            while (!list.isEmpty()) {
                try {
//                    Thread.sleep(10);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println(tName + " removed  " + list.remove(0));
                System.out.println(list.size());
            }
        }

        public static void main(String[] args) {
            // 02. might encounter several problems, including ArrayIndexOutOfBoundsException, duplicate values, skipped values and null values.
            ArrayListRunnable alr = new ArrayListRunnable();
            Thread t1 = new Thread(alr);
            Thread t2 = new Thread(alr);
            Thread t3 = new Thread(alr);
            Thread t4 = new Thread(alr);

            t1.start();
            t2.start();
            t3.start();
            t4.start();
        }

    }


    static class MaxValueCollection {
        // 01. multiple threads can hold the read lock at the same time, but only one thread can hold the write lock at a time.
        private final List<Integer> integers = new ArrayList<>();
        private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

        public void add(Integer i) {
            rwl.writeLock().lock(); // one at time
            try {
                integers.add(i);
            } finally {
                rwl.writeLock().unlock();
            }
        }

        public int findMax(Integer i) {
            rwl.readLock().lock(); // one at time
            try {
                return Collections.max(integers);
            } finally {
                rwl.readLock().unlock();
            }
        }
    }

}
