package io.github.techwithramaa;

import java.util.concurrent.atomic.AtomicInteger;

class FooBar {
    private int n;
    private AtomicInteger turn = new AtomicInteger(0); // 0 = foo, 1 = bar

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (turn.get() != 0) {
                Thread.yield(); // spin-wait until it's foo's turn
            }
            printFoo.run();
            turn.set(1); // pass control to bar
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            if (turn.get() != 1) {
                Thread.yield(); // spin-wait until it's bar's turn
            }
            printBar.run();
            turn.set(0); // pass control to foo
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FooBar fooBar = new FooBar(5);

        Thread t1 = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
