package io.github.techwithramaa;


import java.util.concurrent.locks.*;
import java.util.*;

class BlockingQueue<T> {
    private final LinkedList<T> queue;
    private final int capacity;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    public BlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await(); // Wait if the queue is full
            }
            queue.add(item);  // Add item to the queue
            notEmpty.signalAll(); // Notify consumers that the queue is not empty
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // Wait if the queue is empty
            }
            T item = queue.poll();  // Remove item from the queue
            notFull.signalAll();  // Notify producers that the queue is not full
            return item;
        } finally {
            lock.unlock();
        }
    }
}

public class Main {
    private static final int PRODUCERS_COUNT = 5;
    private static final int CONSUMERS_COUNT = 7;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(10); // Queue capacity of 10 items

        // Create and start 5 producers
        for (int i = 0; i < PRODUCERS_COUNT; i++) {
            int producerId = i + 1;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        int item = producerId * 10 + j;
                        System.out.println("Producer " + producerId + " produced: " + item);
                        queue.put(item);
                        Thread.sleep(100); // Simulate time taken to produce an item
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        Thread.sleep(10000);

        // Create and start 7 consumers
        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            int consumerId = i + 1;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        Integer item = queue.take();
                        System.out.println("Consumer " + consumerId + " consumed: " + item);
                        Thread.sleep(200); // Simulate time taken to consume an item
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}