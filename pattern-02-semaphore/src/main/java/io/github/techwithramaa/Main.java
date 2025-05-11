package io.github.techwithramaa;

import java.util.concurrent.Semaphore;

class Foo {
    private Semaphore secondReady = new Semaphore(0);
    private Semaphore thirdReady = new Semaphore(0);

    public Foo() {}

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        secondReady.release(); // Allow second()
    }

    public void second(Runnable printSecond) throws InterruptedException {
        secondReady.acquire(); // Wait for first()
        printSecond.run();
        thirdReady.release();  // Allow third()
    }

    public void third(Runnable printThird) throws InterruptedException {
        thirdReady.acquire();  // Wait for second()
        printThird.run();
    }
}

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Semaphore Version ===");
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

        // Simulate random thread order
        t3.start();
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
