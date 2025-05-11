package io.github.techwithramaa;

import java.util.function.IntConsumer;

class FizzBuzz {
    private int n;
    private int i = 1;

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public synchronized void fizz(Runnable printFizz) throws InterruptedException {
        while (i <= n) {
            while (i <= n && (i % 3 != 0 || i % 5 == 0))
                wait();
            if (i > n)
                break;

            printFizz.run();
            i++;
            notifyAll();
        }
    }

    // printBuzz.run() outputs "buzz".
    public synchronized void buzz(Runnable printBuzz) throws InterruptedException {
        while (i <= n) {
            while (i <= n && (i % 5 != 0 || i % 3 == 0))
                wait();
            if (i > n)
                break;

            printBuzz.run();
            i++;
            notifyAll();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public synchronized void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (i <= n) {
            while (i <= n && (i % 5 != 0 || i % 3 != 0))
                wait();
            if (i > n)
                break;

            printFizzBuzz.run();
            i++;
            notifyAll();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public synchronized void number(IntConsumer printNumber) throws InterruptedException {
        while (i <= n) {
            while (i <= n && (i % 3 == 0 || i % 5 == 0))
                wait();
            if (i > n)
                break;

            printNumber.accept(i);
            i++;
            notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        // Threads for Fizz, Buzz, FizzBuzz, and Number
        Thread fizzThread = new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> System.out.println("fizz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread buzzThread = new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> System.out.println("buzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread fizzbuzzThread = new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> System.out.println("fizzbuzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread numberThread = new Thread(() -> {
            try {
                fizzBuzz.number(i -> System.out.println(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start threads
        fizzThread.start();
        buzzThread.start();
        fizzbuzzThread.start();
        numberThread.start();

        // Wait for all threads to finish
        fizzThread.join();
        buzzThread.join();
        fizzbuzzThread.join();
        numberThread.join();
    }
}

public class Main {
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        // Threads for Fizz, Buzz, FizzBuzz, and Number
        Thread fizzThread = new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> System.out.println("fizz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread buzzThread = new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> System.out.println("buzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread fizzbuzzThread = new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> System.out.println("fizzbuzz"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread numberThread = new Thread(() -> {
            try {
                fizzBuzz.number(i -> System.out.println(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start threads
        fizzThread.start();
        buzzThread.start();
        fizzbuzzThread.start();
        numberThread.start();

        // Wait for all threads to finish
        try {
        fizzThread.join();
        buzzThread.join();
        fizzbuzzThread.join();
        numberThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}