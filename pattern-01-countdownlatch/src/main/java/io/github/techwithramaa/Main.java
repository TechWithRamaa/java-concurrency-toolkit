package io.github.techwithramaa;

import java.util.concurrent.CountDownLatch;

class Foo {
    private final CountDownLatch latch1 = new CountDownLatch(1); // Synchronization for first and second
    private final CountDownLatch latch2 = new CountDownLatch(1); // Synchronization for second and third

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();  // Print "first"
        latch1.countDown();  // Allow second() to proceed
    }

    public void second(Runnable printSecond) throws InterruptedException {
        latch1.await();  // Wait for first() to complete
        printSecond.run();  // Print "second"
        latch2.countDown();  // Allow third() to proceed
    }

    public void third(Runnable printThird) throws InterruptedException {
        latch2.await();  // Wait for second() to complete
        printThird.run();  // Print "third"
    }


}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();

        // Thread for first()
        Thread t1 = new Thread(() -> {
            try {
                foo.first(() -> System.out.println("first"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread for second()
        Thread t2 = new Thread(() -> {
            try {
                foo.second(() -> System.out.println("second"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread for third()
        Thread t3 = new Thread(() -> {
            try {
                foo.third(() -> System.out.println("third"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start all threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
    }
}