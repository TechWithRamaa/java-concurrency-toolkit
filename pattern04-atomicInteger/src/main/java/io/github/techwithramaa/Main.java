package io.github.techwithramaa;

import java.util.concurrent.atomic.AtomicInteger;

class Foo {
    private AtomicInteger counter = new AtomicInteger(0);

    public Foo() {}

    public void first(Runnable printFirst) throws InterruptedException {
        // No need to wait — first() always runs first
        printFirst.run();
        counter.incrementAndGet(); // Set counter to 1
    }

    public void second(Runnable printSecond) throws InterruptedException {
        // Wait until counter == 1 (i.e., first() has completed)
        while (counter.get() < 1) {
            Thread.yield(); // spin-wait
        }
        printSecond.run();
        counter.incrementAndGet(); // Set counter to 2
    }

    public void third(Runnable printThird) throws InterruptedException {
        // Wait until counter == 2 (i.e., second() has completed)
        while (counter.get() < 2) {
            Thread.yield(); // spin-wait
        }
        printThird.run();
    }
}

public class Main {
    public static void main(String[] args) {

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

        // Start in any order — output will always be "firstsecondthird"
        t3.start();
        t2.start();
        t1.start();
    }
}