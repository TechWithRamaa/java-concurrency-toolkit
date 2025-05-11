# Semaphore-based Thread Synchronization in Java

This project demonstrates how to use **Semaphores** for thread synchronization in Java. The `Foo` class manages the order in which three threads execute using semaphores to control access between the methods `first()`, `second()`, and `third()`.

## Problem Statement

In this implementation, we have three methods:
1. `first()`: Should print "first".
2. `second()`: Should print "second".
3. `third()`: Should print "third".

These methods must be executed in a specific order: **first**, then **second**, and finally **third**. We simulate the concurrent execution of these methods in different threads, and the order of execution is guaranteed by using semaphores for synchronization.

## How it Works

- **Semaphore** objects are used to control the execution order.
- Each method waits for the semaphore of the previous method to be released before proceeding.
- The first method (`first()`) releases a semaphore to allow the second method to run, and similarly, the second method releases a semaphore to allow the third method to run.

## Code Breakdown

1. **`Foo` class**:
    - `secondReady`: A semaphore to ensure `second()` runs only after `first()`.
    - `thirdReady`: A semaphore to ensure `third()` runs only after `second()`.

2. **`first()`**:
    - Runs the provided `Runnable` and releases the `secondReady` semaphore to allow the `second()` method to execute.

3. **`second()`**:
    - Waits for the `secondReady` semaphore, executes its task, and then releases the `thirdReady` semaphore to allow `third()` to run.

4. **`third()`**:
    - Waits for the `thirdReady` semaphore and executes its task.

## Running the Code

### To run the program:
1. Create a new Java class and copy the provided code into the file.
2. Compile and run the class. You will see the printed output in the order of `first`, `second`, and `third`.

### Example Output:
firstsecondthird