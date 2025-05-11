# Print in Order (Using `CountDownLatch`)

## Problem Statement
Design a class `Foo` that ensures three methods are executed in order:
- `first()` prints "first"
- `second()` prints "second"
- `third()` prints "third"

These methods are called from different threads in any order. Ensure the output is always:

firstsecondthird


## Approach: `CountDownLatch`

- `CountDownLatch` is used to synchronize the execution of threads.
- We create two latches:
    - One for ensuring `second()` waits until `first()` completes.
    - One for ensuring `third()` waits until `second()` completes.
- Once a latch reaches zero, the corresponding thread can proceed.

## Sample Output
```bash
=== CountDownLatch Version ===
firstsecondthird
```

## Thread Coordination Logic
- first() executes immediately and then decrements the first latch, allowing second() to proceed.

- second() waits for the first latch to be decremented, prints, and then decrements the second latch, allowing third() to proceed.

- third() waits for the second latch to be decremented and prints the final output.