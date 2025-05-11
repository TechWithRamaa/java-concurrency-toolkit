package io.github.techwithramaa;

import java.util.concurrent.locks.ReentrantLock;

class FixedWindowRateLimiter {

    private final int maxRequests;
    private final long windowSizeInMillis;

    private int requestCount;
    private long windowStart;

    private final ReentrantLock lock = new ReentrantLock(); // optional: pass true for fairness

    public FixedWindowRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestCount = 0;
        this.windowStart = System.currentTimeMillis();
    }

    public boolean allowRequest() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();

            // Reset the window if time has passed
            if (now - windowStart >= windowSizeInMillis) {
                windowStart = now;
                requestCount = 0;
            }

            if (requestCount < maxRequests) {
                requestCount++;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }
}


public class Main {
    public static void main(String[] args) {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 1000); // 5 requests/sec

        Runnable task = () -> {
            while (true) {
                if (limiter.allowRequest()) {
                    System.out.println(Thread.currentThread().getName() + " - allowed");
                } else {
                    System.out.println(Thread.currentThread().getName() + " - blocked");
                }
                try {
                    Thread.sleep(100); // simulate request frequency
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        for (int i = 1; i <= 3; i++) {
            new Thread(task, "Client-" + i).start();
        }
    }
}