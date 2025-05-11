package io.github.techwithramaa;

class Foo {
    // Shared Variable
    private int stage = 1;

    public Foo() {}

    // Critical Section
    public synchronized void first(Runnable printFirst) throws InterruptedException {
        // Run first and move to next stage
        printFirst.run();
        stage = 2;
        notifyAll(); // Wake up other waiting threads
    }

    // Critical Section
    public synchronized void second(Runnable printSecond) throws InterruptedException {
        while (stage < 2) {
            wait(); // Wait until first() completes
        }
        printSecond.run();
        stage = 3;
        notifyAll();
    }

    // Critical Section
    public synchronized void third(Runnable printThird) throws InterruptedException {
        while (stage < 3) {
            wait(); // Wait until second() completes
        }
        printThird.run();
    }
}


public class Main {
    public static void main(String[] args) {
        System.out.println("=== wait/notify Version ===");

        Foo foo = new Foo();

        Thread t1 = new Thread(() -> {
            try {
                foo.first(() -> System.out.print("first"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                foo.second(() -> System.out.print("second"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                foo.third(() -> System.out.print("third"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start in random order to simulate race condition
        t3.start();
        t2.start();
        t1.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(); // move to new line
    }
}