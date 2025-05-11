package io.github.techwithramaa;

import java.util.concurrent.*;
import java.util.*;

class H2O {
    private Semaphore hydrogenSem = new Semaphore(2);
    private Semaphore oxygenSem = new Semaphore(1);
    private CyclicBarrier barrier = new CyclicBarrier(3);

    public H2O() {}

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogenSem.acquire();
        try {
            barrier.await(); // wait for 2H + 1O
            releaseHydrogen.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hydrogenSem.release();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygenSem.acquire();
        try {
            barrier.await(); // wait for 2H + 1O
            releaseOxygen.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            oxygenSem.release();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String water = "OOHHHH";

        int countH = 0, countO = 0;
        for (char c : water.toCharArray()) {
            if (c == 'H') countH++;
            else if (c == 'O') countO++;
        }

        int moleculeCount = Math.min(countH / 2, countO);
        StringBuffer output = new StringBuffer();
        H2O h2o = new H2O();

        Runnable releaseH = () -> output.append("H");
        Runnable releaseO = () -> output.append("O");

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < moleculeCount * 2; i++) {
            threads.add(new Thread(() -> {
                try {
                    h2o.hydrogen(releaseH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }

        for (int i = 0; i < moleculeCount; i++) {
            threads.add(new Thread(() -> {
                try {
                    h2o.oxygen(releaseO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }

        // Shuffle to simulate random thread scheduling
        Collections.shuffle(threads);

        for (Thread t : threads)
            t.start();

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Final Output: " + output.toString());
    }
}