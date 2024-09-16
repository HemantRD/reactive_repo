package com.example.demo.stream.threadchap;

public class Threads {

    public static void main(String[] args) throws InterruptedException {
        // example notifyAll() method
        NotifyAllCalc calculator = new NotifyAllCalc();

        /*Actually, the way the OP's code is written, the other threads are not waiting,
        so they never see a notification. See my answer for a detailed analysis (and a fix).
        try removing below line and test
        Thread.sleep(2000);
         */

        new Thread(new NotifyAllReaders(calculator)).start();
        new Thread(new NotifyAllReaders(calculator)).start();
        new Thread(new NotifyAllReaders(calculator)).start();


        Thread.sleep(2000);
        new Thread(calculator).start();

    }

    public static void main12(String[] args) throws Exception {
        // wait() and notify() method example
        NotifyThread thread = new NotifyThread();
        Thread threadB = new Thread(thread);
        threadB.start();

        synchronized (threadB) {
            System.out.println("Waiting for threadB to complete...");
            threadB.wait();
            System.out.println(thread.total);
        }

    }

    public static void main11(String[] args) {
        // race conditions example. same seat booked by multiple threads
        Threads threads = new Threads();
        threads.go();
    }

    public void go() {
        Thread getSeats1 = new Thread(() -> {
            ticketAgentBooks("1A");
            ticketAgentBooks("1B");
        });
        Thread getSeats2 = new Thread(() -> {
            ticketAgentBooks("1A");
            ticketAgentBooks("1B");
        });
        getSeats1.start();
        getSeats2.start();
    }

    public void ticketAgentBooks(String seat) {
        Show show = Show.getInstance();
        System.out.println(Thread.currentThread().getName() + ": " + show.bookSeat(seat));
    }

    public static void main10(String[] args) {
        // can throw IndexOutOfBoundsException sometimes
        final NameList nl = new NameList();
        nl.add("Ozymandias");
        class NameDropper extends Thread {
            @Override
            public void run() {
                String name = nl.removeFirst();
                System.out.println(name);
            }
        }
        Thread t1 = new NameDropper();
        Thread t2 = new NameDropper();
        Thread t3 = new NameDropper();
        Thread t4 = new NameDropper();
        Thread t5 = new NameDropper();
        Thread t6 = new NameDropper();
        Thread t7 = new NameDropper();
        Thread t8 = new NameDropper();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
    }

    public static void main9(String[] args) {
        // get lock on StringBuffer object so that it get protected by three simultaneous threads
        StringBuffer sb = new StringBuffer("A");
        new InSync(sb).start();
        new InSync(sb).start();
        new InSync(sb).start();
    }

    static Account acct = new Account();

    public static void main8(String[] args) {
        // account overdrawn by 10 without synchronized keyword in makeWithdrawal method.
        // and if we add it, it will work as expected
        Runnable accountDanger = () -> {
            for (int x = 0; x < 5; x++) {
                makeWithdrawal(10);
                if (acct.getBalance() < 0) {
                    System.out.println("account is overdrawn!");
                }
            }
        };
        Thread one = new Thread(accountDanger);
        Thread two = new Thread(accountDanger);
        one.setName("Fred");
        two.setName("Lucy");
        one.start();
        two.start();
    }

    private synchronized static void makeWithdrawal(int amount) {
        if (acct.getBalance() >= amount) {
            System.out.println(Thread.currentThread().getName() + " is going to withdraw");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            acct.withdraw(amount);
            System.out.println(Thread.currentThread().getName() + " completes the withdrawal");
        } else {
            System.out.println("Not enough in account for " + Thread.currentThread().getName() + " to withdraw " + acct.getBalance());
        }
    }

    public static void main7(String[] args) throws Exception {
        // join() method example. Join me (the current thread) to the end of t, so that t must finish
        // before I (the current thread) can run again.
        Runnable r1 = () -> {
            for (int i = 1; i <= 50; i++) {
                System.out.println(Thread.currentThread().getName() + " is running !!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable r = () -> {
            try {
                Thread B = new Thread(r1);
                B.setName("B");
                B.start();
                B.join();
                for (int i = 1; i <= 50; i++) {
                    System.out.println(Thread.currentThread().getName() + " is running !");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Errors..." + e.getMessage());
            }
        };
        Thread A = new Thread(r);
        A.setName("A");
        A.start();
        System.out.println("Completed...");
    }

    public static void main6(String[] args) {
        // print 1 to 100 and print string every 10th time
        Thread thread = new Thread(new NameRunnable3());
        thread.start();
    }

    public static void main5(String[] args) {
        NameRunnable2 nr = new NameRunnable2();

        Thread one = new Thread(nr);
        one.setName("Fred");
        Thread two = new Thread(nr);
        two.setName("Lucy");
        Thread three = new Thread(nr);
        three.setName("Ricky");

        one.start();
        two.start();
        three.start();

    }

    public static void main4(String[] args) {
        Runnable r = () -> {
            for (int i = 1; i <= 400; i++) {
                System.out.println(Thread.currentThread().getName() + " => " + i + " -> " + Thread.currentThread().getId());
            }
        };
        Thread t1 = new Thread(r);
        t1.setName("Fred");
        t1.start();
        Thread t2 = new Thread(r);
        t2.setName("Lucy");
        t2.start();
        Thread t3 = new Thread(r);
        t3.setName("Ricky");
        t3.start();
    }

    public static void main3(String[] args) {
        // give thread a name
        NameRunnable r = new NameRunnable();
        Thread t = new Thread(r);
        t.setName("Fred");
        t.start();
        // get main thread name
        System.out.println(Thread.currentThread().getName());
    }

    public static void main2(String[] args) {
        // same target for multiple threads
        Runnable run = () -> {
            System.out.println("in background");
        };
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
        // define instantiate and start the thread
        Runnable r = () -> {
            for (int i = 1; i <= 6; i++) {
                System.out.println("Runnable running " + i);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public static void main1(String[] args) {
        // Three ways to create the threads
        Hello t1 = new Hello();
        t1.start();
        Thread t2 = new Hello();
        t2.start();
        Runnable run = () -> {
            System.out.println("in background...");
        };
        Thread th = new Thread(run);
        th.start();
    }

    static class Hello extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                System.out.println(i);
            }
        }
    }
}
