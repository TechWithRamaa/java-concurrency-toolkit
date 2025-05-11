package io.github.techwithramaa;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

class FizzBuzz {

    private int n;
    private AtomicInteger i = new AtomicInteger(1);

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) {
        while (i.get() <= n) {
            if (i.get() % 3 == 0 && i.get() % 5 != 0) {
                printFizz.run();
                i.incrementAndGet(); // increment atomic integer safely
            } else {
                Thread.yield();
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) {
        while (i.get() <= n) {
            if (i.get() % 5 == 0 && i.get() % 3 != 0) {
                printBuzz.run();
                i.incrementAndGet(); // increment atomic integer safely
            } else {
                Thread.yield();
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) {
        while (i.get() <= n) {
            if (i.get() % 3 == 0 && i.get() % 5 == 0) {
                printFizzBuzz.run();
                i.incrementAndGet(); // increment atomic integer safely
            } else {
                Thread.yield();
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) {
        while (i.get() <= n) {
            if (i.get() % 3 != 0 && i.get() % 5 != 0) {
                printNumber.accept(i.get());
                i.incrementAndGet(); // increment atomic integer safely
            } else {
                Thread.yield();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        // Threads for Fizz, Buzz, FizzBuzz, and Number
        Thread fizzThread = new Thread(() -> fizzBuzz.fizz(() -> System.out.println("fizz")));
        Thread buzzThread = new Thread(() -> fizzBuzz.buzz(() -> System.out.println("buzz")));
        Thread fizzbuzzThread = new Thread(() -> fizzBuzz.fizzbuzz(() -> System.out.println("fizzbuzz")));
        Thread numberThread = new Thread(() -> fizzBuzz.number(i -> System.out.println(i)));

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