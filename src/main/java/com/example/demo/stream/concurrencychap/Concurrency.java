package com.example.demo.stream.concurrencychap;

import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Concurrency {
    public static final int SIZE = 400000000;
    public static final int THRESHOLD = 1000;
    public static final int MAX = 10;
    public static final int NUM = 5;


    public static void main(String[] args) throws Exception {
        // compare performance parallel, forkJoin and for loop
        int[] data2sum = new int[SIZE];
        long sum = 0, startTime, endTime, duration;
        for (int i = 0; i < SIZE; i++) {
            data2sum[i] = ThreadLocalRandom.current().nextInt(MAX) + 1;
        }
        startTime = Instant.now().toEpochMilli();
        // sum numbers with plain old for loop
        for (int i = 0; i < data2sum.length; i++) {
            if (data2sum[i] > NUM) {
                sum = sum + data2sum[i];
            }
        }
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("Summed with for loop in " + duration
                + " milliseconds; sum is: " + sum);

        // sum numbers with RecursiveTask
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        SumRecursiveTask action = new SumRecursiveTask(data2sum, 0, data2sum.length);
        startTime = Instant.now().toEpochMilli();
        sum = forkJoinPool.invoke(action);
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("Summed with recursive task in "
                + duration + " milliseconds; sum is: " + sum);

        // sum numbers with a parallel stream
        IntStream stream2sum = IntStream.of(data2sum);
        startTime = Instant.now().toEpochMilli();
        sum =
                stream2sum
                        .unordered()
                        .parallel()
                        .filter(i -> i > NUM)
                        .sum();
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("Stream data summed in " + duration
                + " milliseconds; sum is: " + sum);

        // sum numbers with a parallel stream, limiting workers
        ForkJoinPool fjp2 = new ForkJoinPool(4);
        IntStream stream2sum2 = IntStream.of(data2sum);
        startTime = Instant.now().toEpochMilli();
        sum =
                fjp2.submit(
                        () -> stream2sum2
                                .unordered()
                                .parallel()
                                .filter(i -> i > NUM)
                                .sum()
                ).get();
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("FJP4 Stream data summed in "
                + duration + " milliseconds; sum is: " + sum);
    }

    static class SumRecursiveTask extends RecursiveTask<Long> {
        public static final int SIZE = 400000000;
        public static final int THRESHOLD = 1000;
        public static final int MAX = 10;
        public static final int NUM = 5;
        private int[] data;
        private int start;
        private int end;

        public SumRecursiveTask(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long tempSum = 0;
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    if (data[i] > NUM) {
                        tempSum += data[i];
                    }
                }
                return tempSum;
            } else {
                int halfWay = ((end - start) / 2) + start;
                SumRecursiveTask t1 = new SumRecursiveTask(data, start, halfWay);
                SumRecursiveTask t2 = new SumRecursiveTask(data, halfWay, end);
                t1.fork();
                long sum2 = t2.compute();
                long sum1 = t1.join();
                return sum2 + sum1;
            }
        }
    }

    public static void main19(String[] args) {
        //This example illustrates that findAny() really does find any result
        IntStream nums = IntStream.range(0, 20);
        OptionalInt any = nums.parallel().peek(i -> System.out.println(i + ": " + Thread.currentThread().getName())).filter(
                i -> i % 2 == 0 ? true : false
        ).findAny();
        any.ifPresent(System.out::println);
    }

    public static void main18(String[] args) {
        // check stream is ordered or not.
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Stream<Integer> s = nums.stream();
        System.out.println("Stream from List ordered? " + s.spliterator().hasCharacteristics(Spliterator.ORDERED));
        //We can explicitly tell the stream to not worry about remaining ordered by calling the unordered()
        List<Integer> nums1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        long sum = nums1.stream()
                .unordered() // make the stream unordered
                .parallel()
                .mapToInt(n -> n)
                .filter(i -> i % 2 == 0 ? true : false)
                .sum();
        System.out.println("Sum of evens is: " + sum);
    }

    public static void main17(String[] args) throws ExecutionException, InterruptedException {
        // with parallel and 2 threads same benefit
        final int SIZE = 400000000;
        final int LIMIT = 5;
        long sum = 0, startTime, endTime, duration;
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        IntStream stream = IntStream.range(0, SIZE);
        startTime = Instant.now().toEpochMilli();
        sum = forkJoinPool.submit(() -> stream.parallel().limit(LIMIT).sum()).get();
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("FJP Stream data summed in " + duration + " miliseconds; sum is: " + sum);
    }

    public static void main16(String[] args) throws ExecutionException, InterruptedException {
        // without parallel same benefit
        final int SIZE = 400000000;
        final int LIMIT = 5;
        long sum = 0, startTime, endTime, duration;
        ForkJoinPool forkJoinPool = new ForkJoinPool(1);
        IntStream stream = IntStream.range(0, SIZE);
        startTime = Instant.now().toEpochMilli();
        sum = forkJoinPool.submit(() -> stream.limit(LIMIT).sum()).get();
        endTime = Instant.now().toEpochMilli();
        duration = endTime - startTime;
        System.out.println("FJP Stream data summed in " + duration + " miliseconds; sum is: " + sum);
    }

    public static void main15(String[] args) {
        // running without parallel stream
        final int SIZE = 1000000;
        final int LIMIT = 5;
        long sum = 0, statTime, endTime, duration;
        IntStream stream = IntStream.range(0, SIZE);
        statTime = Instant.now().toEpochMilli();
        sum = stream.limit(LIMIT).sum();
        endTime = Instant.now().toEpochMilli();
        duration = statTime = endTime;
        System.out.println("Items summed in " + duration
                + " milliseconds; sum is: " + sum);
        // running with parallel stream hut the performance. atleast limit will not help here..
        stream = IntStream.range(0, SIZE);
        statTime = Instant.now().toEpochMilli();
        sum = stream.parallel().limit(LIMIT).sum();
        endTime = Instant.now().toEpochMilli();
        duration = statTime = endTime;
        System.out.println("Items summed in " + duration
                + " milliseconds; sum is: " + sum);
    }

    public static void main14(String[] args) {
        // stateful count increment is not thread safe. each run differ the result
        class Count {
            int counter = 0;
        }
        Count count = new Count();
        IntStream stream = IntStream.range(0, 10000);
        int sum = stream.parallel().filter(i -> {
            if (i % 10 == 0) {
                count.counter++;
                return true;
            }
            return false;
        }).sum();
        System.out.println("sum: " + sum + ", count: " + count.counter);

    }

    public static void main13(String[] args) throws ExecutionException, InterruptedException {
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
