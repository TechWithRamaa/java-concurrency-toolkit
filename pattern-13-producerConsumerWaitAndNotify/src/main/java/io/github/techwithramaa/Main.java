package io.github.techwithramaa;


import java.util.LinkedList;
import java.util.Queue;

class BlockingQueue {
    private final int capacity;
    private final Queue<Integer> queue;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void put(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // wait if full
        }
        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " produced: " + item);
        notifyAll(); // notify waiting threads
    }

    public synchronized int take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // wait if empty
        }
        int item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " consumed: " + item);
        notifyAll(); // notify waiting threads
        return item;
    }
}

// Single Producer & Consumer
/*public*/ class Main1 {
    public static void main(String[] args) {
        BlockingQueue queue = new BlockingQueue(5);

        // Producer thread using lambda
        Thread producer = new Thread(() -> {
            try {
                while (true) {
                    int item = (int) (Math.random() * 100);
                    queue.put(item);
                    Thread.sleep(1000); // simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        // Consumer thread using lambda
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    queue.take();
                    Thread.sleep(1500); // simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BlockingQueue queue = new BlockingQueue(5);

        // Start 3 producers using lambdas
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                int count = 0;
                try {
                    while (true) {
                        int item = (int) (Math.random() * 100);
                        queue.put(item);
                        Thread.sleep(1000); // simulate work
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + id).start();
        }

        // Start 3 consumers using lambdas
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    while (true) {
                        queue.take();
                        Thread.sleep(1500); // simulate work
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer-" + id).start();
        }
    }
}