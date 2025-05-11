package io.github.techwithramaa;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true); // Fairness enabled
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static int sharedData = 0;

    public static void main(String[] args) {
        // Start 3 Readers
        for (int i = 1; i <= 3; i++) {
            int readerId = i;
            new Thread(() -> {
                while (true) {
                    readLock.lock();
                    try {
                        System.out.println("Reader " + readerId + " reads: " + sharedData);
                        Thread.sleep(100); // Simulate reading
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        readLock.unlock();
                    }
                    sleepQuietly(200);
                }
            }, "Reader-" + i).start();
        }

        // Start 1 Writer
        new Thread(() -> {
            int writerId = 1;
            while (true) {
                writeLock.lock();
                try {
                    sharedData++;
                    System.out.println("Writer " + writerId + " writes: " + sharedData);
                    Thread.sleep(200); // Simulate writing
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    writeLock.unlock();
                }
                sleepQuietly(500);
            }
        }, "Writer-1").start();
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}