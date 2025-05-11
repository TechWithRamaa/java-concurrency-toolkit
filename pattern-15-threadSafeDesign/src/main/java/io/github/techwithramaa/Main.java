package io.github.techwithramaa;

import java.util.concurrent.locks.ReentrantLock;

class UnsafeCounter {
    private int count = 0;

    public void increment() {
        count++; // no synchronization
    }

    public int getCount() {
        return count;
    }
}

class SafeCounter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Uncomment either to test
        //UnsafeCounter counter = new UnsafeCounter();
        SafeCounter counter = new SafeCounter();

        Runnable task = () -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final count = " + counter.getCount());
    }
}