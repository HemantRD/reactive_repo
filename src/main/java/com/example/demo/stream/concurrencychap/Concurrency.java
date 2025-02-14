package com.example.demo.stream.concurrencychap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Concurrency {

    public static void main(String[] args) {
        // shutdown executor different ways
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.shutdown(); // no more new tasks but finish existing tasks

        // wait 2 seconds for running tasks to finish
        try {
            boolean term = executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex1) {
            // did not wait the full 2 seconds
        } finally {
            if (!executorService.isTerminated()) // are all tasks done?
            {
                List<Runnable> unfinished = executorService.shutdownNow();// a collection of the unfinished tasks
            }
        }
    }

    public static void main7(String[] args) {
        // Callable example
        Callable<Integer> callable = () -> {
            int count = ThreadLocalRandom.current().nextInt(1, 10);
            for (int i = 1; i <= count; i++) {
                Thread.sleep(5000);
                System.out.println("Running...." + i);
            }
            return count;
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(callable);
        try {
            Integer result = future.get();
            System.out.println("Result is :" + result);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error" + e.getMessage());
        }
        executorService.shutdown();
    }

    public static void main6(String[] args) {
        //ScheduledExecutorService
        Runnable r = () -> {
            System.out.println("waiting for 3 seconds to complete the task");
            try {
                Thread.sleep(6000);
                System.out.println(LocalDateTime.now() + " My name is " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // run once after a delay of 10 seconds
        // scheduledExecutorService.schedule(r, 10, TimeUnit.SECONDS);
        // begin after 2 seconds delay and begin again every 5 seconds
        //scheduledExecutorService.scheduleAtFixedRate(r, 2, 5, TimeUnit.SECONDS);
        // begin after 2 seconds delay and begin again 5 seconds after completing last execution
        scheduledExecutorService.scheduleWithFixedDelay(r, 2, 5, TimeUnit.SECONDS);

    }

    public static void main5(String[] args) {
        // newSingleThreadExecutor example
        Runnable runnable = () -> {
            System.out.println("My Name is " + Thread.currentThread().getName() + " id -> " + Thread.currentThread().getId());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
        service.execute(runnable);
        service.shutdown();
    }

    public static void main4(String[] args) {
        // newFixedThreadPool example
        Runnable runnable = () -> {
            System.out.println("My Name is " + Thread.currentThread().getName() + " id -> " + Thread.currentThread().getId());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(runnable);
        service.execute(runnable);
        service.shutdown();
    }

    public static void main3(String[] args) {
        // newCachedThreadPool example
        Runnable runnable = () -> {
            System.out.println("My Name is " + Thread.currentThread().getName() + " id -> " + Thread.currentThread().getId());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(runnable);
        service.execute(runnable);
        service.shutdown();
    }


    public static void main2(String[] args) {
        // custom executor example
        Runnable runnable = () -> {
            System.out.println("My Name is " + Thread.currentThread().getName() + " id -> " + Thread.currentThread().getId());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Executor executor = new NewThreadExecutor();
        executor.execute(runnable);
        executor.execute(runnable);
    }

    static class NewThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            Thread t = new Thread(command);
            t.start();
        }
    }

    public static void main1(String[] args) throws Exception {
        // CyclicBarrier example
        List<String> result = new ArrayList<>();
        String[] dogs1 = {"boi", "clover", "charis"};
        String[] dogs2 = {"aiko", "zooey", "biscuit"};

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
