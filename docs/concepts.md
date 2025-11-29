# Thread Basics

## Introduction

A thread is like a helper worker in your program.

Normally your program has one worker (the main thread). If you want something to run at the same time (parallel), you give it to another thread.

So you need a way to describe what the worker should do. That "description" is a task.

Java provides two ways to describe a task:
- Runnable
- Callable

## 1. Runnable (Basic Task)

A Runnable is a task with only one ability: do something.

**Characteristics:**
- Can execute code
- No return value
- Cannot throw checked exceptions

**The method signature:**
```java
void run();
```

**Example:**
```java
Runnable r = () -> {
    System.out.println("Task running in another thread");
};
```

This is like telling a worker: "Do this, but I don't need anything back."

## 2. Callable (Advanced Task)

A Callable is a task with extra powers:
- It can return a value
- It can throw exceptions

**The method signature:**
```java
V call() throws Exception;
```

**Example:**
```java
Callable<Integer> c = () -> {
    Thread.sleep(1000);
    return 10;  // returns something
};
```

## 3. Why Java Created Two Interfaces?

Because not all tasks need to return something.

**Runnable is for "Just do it"**

Examples:
- Logging
- Saving a file
- Sending an email

You don't care about return values.

**Callable is for "Do it and bring me a result"**

Examples:
- Image processing
- Database queries
- Complex calculations
- Anything where a result is needed

## 4. How Threads Use Runnable and Callable

**You submit a Runnable to a thread:**
```java
new Thread(runnable).start();
```

**You submit a Callable to a thread pool:**
```java
Future<Integer> f = executor.submit(callable);
```

`Future.get()` gives you the return value.

## 5. What is a Future?

A Future is like a receipt that says: "Your task is being processed. Come back later to get the result."

**When you submit a Callable:**
```java
Future<ImageData> f = executor.submit(callableTask);
```

You get a `Future<ImageData>`.

**Later:**
```java
ImageData data = f.get();
```

`f.get()` waits until the worker finishes and returns the image tile.

## 6. Why Callable + Future Always Go Together?

Because:
- Callable returns a result
- Future captures that result later

**Summary:**
- Runnable doesn't return anything → future is useless
- Callable returns something → future is useful

**In your project:**
- Each tile-processing task returns ImageData
- So you NEED Callable
- And you NEED Future to retrieve the tile

## 7. What Is a Thread Pool?

Creating many threads manually is BAD because it is:
- Slow
- Expensive
- Wastes memory
- Can crash system

So Java provides a thread pool:
```java
ExecutorService executor = Executors.newFixedThreadPool(100);
```

**How it works:**
- 100 workers already created
- You submit tasks to them
- They reuse threads (no need to create new ones)

It's like a factory with 100 workers. You hand tasks to them; they pick tasks from the queue.
