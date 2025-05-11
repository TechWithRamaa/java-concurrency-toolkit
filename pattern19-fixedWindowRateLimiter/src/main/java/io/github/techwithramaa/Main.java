package io.github.techwithramaa;

class FixedWindowRateLimiter {

    private final int maxRequests; // Maximum number of requests allowed in a window
    private final long windowSizeInMillis; // Duration of the window in milliseconds
    private int requestCount; // Tracks the number of requests in the current window
    private long windowStart; // Timestamp when the current window started

    public FixedWindowRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestCount = 0;
        this.windowStart = System.currentTimeMillis();
    }

    // Check if the request is allowed based on the fixed window algorithm
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        // If the current window has expired, reset the count and start a new window
        if (now - windowStart >= windowSizeInMillis) {
            windowStart = now; // reset window start time
            requestCount = 0; // reset the request count
        }

        // Allow the request if the count is less than the max allowed requests
        if (requestCount < maxRequests) {
            requestCount++; // increment the request count
            return true; // request is allowed
        } else {
            return false; // request is blocked due to rate limiting
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 1000);

        Runnable task = () -> {
            while (true) {
                if (limiter.allowRequest()) {
                    System.out.println(Thread.currentThread().getName() + " - allowed");
                } else {
                    System.out.println(Thread.currentThread().getName() + " - blocked");
                }

                try {
                    Thread.sleep(100); // simulate user request every 100ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Start 3 threads making requests
        for (int i = 1; i <= 3; i++) {
            new Thread(task, "Client-" + i).start();
        }
    }
}