# Examples

## 1. Creating a pooling
 

One of the common usages of **this library** (*[ThreadingTools](https://github.com/Javinator9889/ThreadingTools)*)
 is to concurrently run **a predefined number of threads**, but specifying upper limits (for not 
 running all threads at the same time).
 
 *ThreadingTools* provides a utility for generating a **pool** of threads that will run 
 concurrently, `ThreadsPooling`, accessed via `Builder` inner class.
 
For creating a new instance, we will use:

  + `builder()`, for obtaining the builder object.
  + `build()`, for creating the `ThreadsPooling` class.
  
All other methods are detailed and specified at the 
[official documentation](https://javinator9889.github.io/ThreadingTools/com/github/javinator9889/threading/pools/ThreadsPooling.Builder.html#method.detail)
which explains everything you need to know for creating a new `ThreadsPooling` class.

----------------

```java
import com.github.javinator9889.threading.pools.ThreadsPooling;


public class ExampleClass {
    private ThreadsPooling mPooling;
    
    public ExampleClass() {
        // Constructor that initializes the ThreadsPooling object
        
        // In this example, we will use the number of processor
        // for defining the concurrent jobs running.
        // Also, the maximum jobs running will be twice number 
        // of processor.
        int numberOfProcessors = getNumberOfProcessors();
        int maxProcessRunning = numberOfProcessor * 2;
        // We are going to execute at most 20 threads
        int queueCapacity = 20;
        // We want threads to terminate when they become idle
        // after one second
        long keepAliveTime = 1L;
        
        // We are leaving ThreadFactory and 
        // RejectedExecutionHandler to their defaults
        mPooling = ThreadsPooling.builder()
            .withConcurrentThreadsRunning(numberOfProcessors)
            .withMaximumPoolSize(maxProcessRunning)
            .withQueueCapacity(queueCapacity)
            .withKeepAliveInSeconds(keepAliveTime)
            .build();
        
        // Now we have the ThreadsPooling object created
        // We could have add the threads we want to execute
        // with the methods: "withThread()" and "withThreads"
    }
    
    // Method for getting the generated ThreadsPooling
    public ThreadsPooling getPooling() {
        return mPooling;
    }
}
```