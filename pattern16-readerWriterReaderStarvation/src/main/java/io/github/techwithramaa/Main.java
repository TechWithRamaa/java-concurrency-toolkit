package io.github.techwithramaa;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReaderWriterExample {
    private int data = 0;

    // Read-write lock
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    // Read method (can be called by multiple readers)
    public void read(String readerName) {
        rwLock.readLock().lock();
        try {
            System.out.println(readerName + " is reading: " + data);
            Thread.sleep(100); // Simulate time taken to read
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    // Write method (only one writer at a time)
    public void write(String writerName, int value) {
        rwLock.writeLock().lock();
        try {
            System.out.println(writerName + " is writing: " + value);
            data = value;
            Thread.sleep(200); // Simulate time taken to write
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ReaderWriterExample shared = new ReaderWriterExample();

        // Start writer threads
        for (int i = 1; i <= 2; i++) {
            final int val = i * 10;
            final String name = "Writer-" + i;
            new Thread(() -> {
                shared.write(name, val);
            }).start();
        }

        // Start reader threads
        for (int i = 1; i <= 5; i++) {
            final String name = "Reader-" + i;
            new Thread(() -> {
                shared.read(name);
            }).start();
        }
    }}