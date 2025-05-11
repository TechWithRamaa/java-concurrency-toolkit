package io.github.techwithramaa;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class BlockingQueue<T> {
    private final Queue<T> queue;
    private final int capacity;
    private final ReentrantLock lock;
    private final Condition notEmpty;  // Condition to wait when queue is empty
    private final Condition notFull;   // Condition to wait when queue is full

    public BlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    // Method to put an element into the queue
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            // Wait until the queue is not full
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.offer(item);  // Add the item to the queue
            notEmpty.signal();  // Notify any waiting consumer thread
        } finally {
            lock.unlock();
        }
    }

    // Method to take an element from the queue
    public T take() throws InterruptedException {
        lock.lock();
        try {
            // Wait until the queue is not empty
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T item = queue.poll();  // Remove and return the first element
            notFull.signal();  // Notify any waiting producer thread
            return item;
        } finally {
            lock.unlock();
        }
    }

    // Method to get the size of the queue
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(1000); // Simulate time taken to produce an item
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer item = queue.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(1500); // Simulate time taken to consume an item
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        Thread.sleep(10000); // demonstrates full queue
        consumer.start();

        producer.join();
        consumer.join();
    }
}