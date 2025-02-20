package com.example.demo.stream.concurrencychap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Concurrency {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // parallel stream with ForkJoinPool
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        int sum = forkJoinPool.submit(() ->
                numList.stream().parallel().peek(i -> System.out.println(":" + Thread.currentThread().getName() + " is"))
                        .mapToInt(r -> r).sum()).get();
        int sum2 = forkJoinPool.submit(() ->
                numList.parallelStream().peek(i -> System.out.println(":" + Thread.currentThread().getName() + " is"))
                        .mapToInt(r -> r).sum()).get();
        System.out.println("FJP with 2 workers, sum is :" + sum + sum2);
    }

    public static void main12(String[] args) {
        // parallel stream print thread name
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int sum = numList.stream().parallel().peek(r -> System.out.println("My name is :" + Thread.currentThread().getName()))
                .mapToInt(r -> r).sum();
        System.out.println(sum);
    }

    public static void main11(String[] args) {
        // sort big array using forkJoin
        int[] data = new int[10000000];
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        RandomInitRecursiveAction action = new RandomInitRecursiveAction(data, 0, data.length);
        forkJoinPool.invoke(action);
        Arrays.stream(data).forEach(r -> System.out.print(r + ","));

        ForkJoinPool forkJoinPool2 = new ForkJoinPool(2);
        SortRecursiveAction action2 = new SortRecursiveAction(data, 0, data.length);
        forkJoinPool2.invoke(action2);
        System.out.println("====================");
        System.out.println("====================");
        Arrays.stream(data).forEach(r -> System.out.print(r + ","));

    }

    static class SortRecursiveAction extends RecursiveAction {
        private static final int THRESHOLD = 1000;
        private int data[];
        private int start;
        private int end;

        public SortRecursiveAction(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                Arrays.sort(data, start, end);
            } else {
                int halfWay = ((end - start) / 2) + start;
                SortRecursiveAction a1 = new SortRecursiveAction(data, start, halfWay);
                SortRecursiveAction a2 = new SortRecursiveAction(data, halfWay, end);
                invokeAll(a1, a2);
                if (data[halfWay - 1] <= data[halfWay]) {
                    return;
                }
                int[] temp = new int[end - start];
                int s1 = start, s2 = halfWay, d = 0;
                while (s1 < halfWay && s2 < end) {
                    if (data[s1] < data[s2]) {
                        temp[d++] = data[s1++];
                    } else if (data[s1] > data[s2]) {
                        temp[d++] = data[s2++];
                    } else {
                        temp[d++] = data[s1++];
                        temp[d++] = data[s2++];
                    }
                }
                if (s1 != halfWay) {
                    System.arraycopy(data, s1, temp, d, temp.length - d);
                } else if (s2 != end) {
                    System.arraycopy(data, s2, temp, d, temp.length - d);
                }
                System.arraycopy(temp, 0, data, start, temp.length);
            }
        }
    }

    public static void main10(String[] args) {
        // Parallel Fork/Join example return value
        int[] data = new int[10000000];
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        RandomInitRecursiveAction action = new RandomInitRecursiveAction(data, 0, data.length);
        forkJoinPool.invoke(action);

        FindMaxPositionRecursiveTask task = new FindMaxPositionRecursiveTask(data, 0, data.length);
        Integer position = forkJoinPool.invoke(task);
        System.out.println("Position :" + position + ", value :" + data[position]);
    }

    static class FindMaxPositionRecursiveTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10000;
        private int[] data;
        private int start;
        private int end;

        public FindMaxPositionRecursiveTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                int position = 0;
                for (int i = start; i < end; i++) {
                    if (data[i] > data[position]) {
                        position = i;
                    }
                }
                return position;
            } else {
                int halfway = ((end - start) / 2) + start;
                FindMaxPositionRecursiveTask t1 = new FindMaxPositionRecursiveTask(data, start, halfway);
                t1.fork();
                FindMaxPositionRecursiveTask t2 = new FindMaxPositionRecursiveTask(data, halfway, end);
                int position2 = t2.compute();
                int position1 = t1.join();
                if (data[position1] > data[position2]) {
                    return position1;
                } else if (data[position2] > data[position1]) {
                    return position2;
                } else {
                    return position1 < position2 ? position1 : position2;
                }
            }
        }
    }

    public static void main9(String[] args) {
        // Parallel Fork/Join example
        int[] data = new int[10000000];
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        RandomInitRecursiveAction action = new RandomInitRecursiveAction(data, 0, data.length);
        forkJoinPool.invoke(action);
    }

    static class RandomInitRecursiveAction extends RecursiveAction {
        private static final int THRESHOLD = 10000;
        private int[] data;
        private int start;
        private int end;

        public RandomInitRecursiveAction(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    data[i] = ThreadLocalRandom.current().nextInt();
                }
            } else {
                int halfway = ((end - start) / 2) + start;
                RandomInitRecursiveAction a1 = new RandomInitRecursiveAction(data, start, halfway);
                a1.fork();
                RandomInitRecursiveAction a2 = new RandomInitRecursiveAction(data, halfway, end);
                a2.compute();
                a1.join();
            }
        }
    }

    public static void main8(String[] args) {
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
