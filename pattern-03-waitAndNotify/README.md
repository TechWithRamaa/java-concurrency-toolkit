# 📘 Print in Order (Using `wait`/`notify`)

## Problem Statement
Design a class `Foo` that ensures three methods are executed in order:
- `first()` prints "first"
- `second()` prints "second"
- `third()` prints "third"

These methods are called from different threads in any order. Ensure the output is always:

firstsecondthird


## Approach: `wait()` and `notifyAll()`

- We use a shared state variable to track which method can proceed.
- Threads will:
    - `wait()` if it's not their turn.
    - `notifyAll()` once they've finished, waking the next thread.
- This approach uses no external libraries — just basic Java object monitor mechanisms.

## Sample Output
```bash
=== Wait/Notify Version ===
firstsecondthird

```