package io.github.techwithramaa;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedData {
    private int data = 0;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); // Non-fair lock

    public void read(String readerName) {
        rwLock.readLock().lock();
        try {
            System.out.println(readerName + " is reading: " + data);
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void write(String writerName, int value) {
        rwLock.writeLock().lock();
        try {
            System.out.println(writerName + " is writing: " + value);
            data = value;
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SharedData shared = new SharedData();

        // Repeatedly spawn reader threads every 100ms
        new Thread(() -> {
            int readerCount = 1;
            while (true) {
                final String readerName = "Reader-" + readerCount++;
                new Thread(() -> shared.read(readerName)).start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        // Start one writer after some delay
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Let readers flood in
                shared.write("Writer-1", 99); // Will starve
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

/*

Writer starvation occurs when reader threads keep arriving, and the ReentrantReadWriteLock is in non-fair mode.

Non-fair mode allows new readers to acquire the read lock even if a writer is waiting, as long as no write lock is currently held.

If readers continuously acquire the lock, the writer never gets a chance, leading to starvation.

This is solved by using a fair lock, which honors the waiting queue order, letting the writer proceed before new readers.

*/