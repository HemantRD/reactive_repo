package com.example.demo.stream.concurrencychap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Concurrency {

    static String[] dogs1 = {"boi", "clover", "charis"};
    static String[] dogs2 = {"aiko", "zooey", "biscuit"};

    static class ProcessDogs implements Runnable {
        String dogs[];
        CyclicBarrier barrier;

        ProcessDogs(String[] d, CyclicBarrier barrier) {
            dogs = d;
            this.barrier = barrier;
        }

        public void run() {
            for (int i = 0; i < dogs.length; i++) {
                String dogName = dogs[i];
                String newDogName = dogName.substring(0, 1).toUpperCase() + dogName.substring(1);
                dogs[i] = newDogName;
            }
            try {
                if (Thread.currentThread().getName().equalsIgnoreCase("A")) {
                    Thread.sleep(5000);
                }
                if (Thread.currentThread().getName().equalsIgnoreCase("B")) {
                    Thread.sleep(10000);
                }
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " is done!");
        }
    }

    public static void main(String[] args) throws Exception {
        // CyclicBarrier example
        List<String> result = new ArrayList<>();
        //number of threads that will wait at the barrier and a Runnable that will
        //be run by the last thread to reach the barrier
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            for (int i = 0; i < dogs1.length; i++) {
                result.add(dogs1[i]);
            }
            for (int i = 0; i < dogs2.length; i++) {
                result.add(dogs2[i]);
            }
            System.out.println(Thread.currentThread().getName() + " Result:" + result);
        });

        Thread t1 = new Thread(new ProcessDogs(dogs1, barrier));
        t1.setName("A");
        Thread t2 = new Thread(new ProcessDogs(dogs2, barrier));
        t2.setName("B");

        t1.start();
        t2.start();
        System.out.println("Main Thread is done");
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
