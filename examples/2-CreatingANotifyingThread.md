# Examples

## 2. Creating a NotifyingThread

As explained at *README*, there are several ways to know when a **thread** has finished its job:

  + **Using** a `Future` object, in which you define *what is going to be executed* and then, 
  wait for its result (if it has one).<br /><br />
  **Advantages**: you will only have the result when the `Future` is done.<br />
  **Disadvantages**: if there is no result, you will not know when the `Future` is done.
  
  + **Using** a `Thread` object, and call `join()`, blocking your main thread (or *any other 
  thread where `join()` was called*) for a non-defined time.<br /><br />
  **Advantages**: you do not need to use any other lib, it is a *built-in*.<br />
  **Disadvantages**: you dedicate a **hole process** to wait for a thread that maybe do not finish.
  
With `NotifyingThread`, all those problems disappear, as:

  + You can **get notified** of the threads *you want to get notified by*.
  + You do not have to **actively wait** for a thread to *finish*.
  + You can use them as `Future`, because until the result is not set, there is nothing at its 
  output.
  
For taking advantage of all those *functionalities*, you just need to **subscribe you class** 
(that must implement `OnThreadCompletedListener`) to the correspondent `NotifyingThread`. If not,
 it is just a simple thread 🤷‍
 
------------------

```java
import com.github.javinator9889.threading.threads.notifyingthread.*;

public class NotifyingThreadExampleClass implements OnThreadCompletedListener {
    // Default constructor - we do not need anything else
    public NotifyingThreadExampleClass() {}
    
    public void executeAThread() {
        // Generate a new NotifyingThread
        // We are using a lambda expression for providing the
        // Runnable class; it is the same as doing:
        //
        // new NotifyingThread(new Runnable() {
        //      @Override
        //      public void run() {
        //          System.out.println("Hi, I am a NotifyingThread");
        //      }
        // });
        NotifyingThread thread = new NotifyingThread(() ->
            System.out.println("Hi, I am a NotifyingThread")
        );
        
        // Subscribe this class (NotifyingThreadExampleClass) 
        // for getting notified whether the thread has finished
        // or it was interrupted by an exception
        thread.addOnThreadCompletedListener(this);
        
        // Setup a custom name for identification
        thread.setName("NotifyingThreadExample");
        
        // Start executing - this is always necessary
        thread.start();
    }
    
    // Custom implementation for knowing that a thread has finished
    // Here, we can wait until a concrete thread has finished for starting
    // another job that requires the thread that called "onThreadCompletedListener"
    // to finish.
    @Override
    public void onThreadCompletedListener(Thread thread, @Nullable Throwable exception) {
        switch (thread.getName()) {
            case "NotifyingThreadExample":
                System.out.println("NotifyingThread finished!");
                if (exception != null) {
                    System.err.println("NotifyingThread threw an exception during execution");
                    exception.printStackTrace();
                } else {
                    System.out.println("NotifyingThread finished without errors");
                    anotherThreadThatRequiresNotifyingThreadToFinish().start();
                }
                break;
            default:
                System.err.println("Mmmmm");
                break;
        }
    }
}
``` 