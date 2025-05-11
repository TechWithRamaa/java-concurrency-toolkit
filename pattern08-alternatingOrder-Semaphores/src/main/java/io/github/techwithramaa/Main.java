package io.github.techwithramaa;

import java.util.concurrent.Semaphore;

class FooBar {
    private final int n;

    private Semaphore fooSem = new Semaphore(1); // initially available
    private Semaphore barSem = new Semaphore(0); // initially unavailable

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooSem.acquire();       // ① block here if no permit
            printFoo.run();         // ② print "foo"
            barSem.release();       // ③ signal bar thread
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barSem.acquire();       // ④ block here until foo signals
            printBar.run();         // ⑤ print "bar"
            fooSem.release();       // ⑥ signal foo thread
        }
    }
}



public class Main {
    public static void main(String[] args) {
        FooBar fooBar = new FooBar(5);

        Thread fooThread = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread barThread = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        fooThread.start();
        barThread.start();

        try {
            fooThread.join();
            barThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}