package io.github.techwithramaa;

class FooBar {
    private int n;
    private boolean fooTurn = true;  // start with foo

    public FooBar(int n) {
        this.n = n;
    }

    public synchronized void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!fooTurn) {
                wait(); // wait until it's foo's turn
            }
            printFoo.run();
            fooTurn = false;
            notifyAll(); // wake up bar
        }
    }

    public synchronized void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (fooTurn) {
                wait(); // wait until it's bar's turn
            }
            printBar.run();
            fooTurn = true;
            notifyAll(); // wake up foo
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