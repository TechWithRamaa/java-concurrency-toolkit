package io.github.techwithramaa;


import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final int NUM_PHILOSOPHERS = 5;
    private static final ReentrantLock[] forks = new ReentrantLock[NUM_PHILOSOPHERS];

    static {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new ReentrantLock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            final int id = i;
            new Thread(() -> {
                while (true) {
                    think(id);

                    // Deadlock prevention: let last philosopher pick up right fork first
                    if (id == NUM_PHILOSOPHERS - 1) {
                        pickUpFork(id, (id + 1) % NUM_PHILOSOPHERS); // right then left
                    } else {
                        pickUpFork((id + 1) % NUM_PHILOSOPHERS, id); // left then right
                    }

                    eat(id);

                    // Put down forks
                    forks[id].unlock();
                    forks[(id + 1) % NUM_PHILOSOPHERS].unlock();
                }
            }, "Philosopher-" + id).start();
        }
    }

    private static void pickUpFork(int first, int second) {
        forks[first].lock();
        forks[second].lock();
    }

    private static void think(int id) {
        System.out.println("Philosopher " + id + " is thinking.");
        sleep(500);
    }

    private static void eat(int id) {
        System.out.println("Philosopher " + id + " is eating.");
        sleep(500);
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}