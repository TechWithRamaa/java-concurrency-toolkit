# Print in Order – AtomicInteger Version

This project demonstrates how to coordinate three threads (`first()`, `second()`, and `third()`) so they execute in the correct order using **`AtomicInteger`** for thread synchronization.

---

## Problem Statement

Design a class `Foo` that ensures the following methods are executed in the correct order:

- `first()` → prints `"first"`
- `second()` → prints `"second"`
- `third()` → prints `"third"`

These methods may be called from separate threads and in **any order**, but the output must **always be**:  

firstsecondthird

---

## Approach: AtomicInteger

We use an `AtomicInteger` called `counter` to track how many methods have been completed:

- Initial value: `0`
- After `first()` completes → counter = `1`
- After `second()` completes → counter = `2`

The `second()` and `third()` methods **wait in a spin loop** using `while (...) Thread.yield()` until the required condition is met.

---