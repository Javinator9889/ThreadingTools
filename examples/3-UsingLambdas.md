# Examples

## 3. Using lambdas

`Thread` does not allow us to **change its runnable** once it has been created, which is not a 
problem (is very strange to change that once a thread is created) but can be.

If you have read the [documentation about NotifyingThread](https://javinator9889.github.io/ThreadingTools/com/github/javinator9889/threading/threads/notifyingthread/NotifyingThread.html),
you have noticed that there are **four methods** for setting an executable *once the class is 
created*. Those methods are:

  + `setExecutable(Runnable runnable)`
  + `setExecutable(Consumer<ArgumentParser> consumer, ArgumentParser args)`
  + `setExecutable(Function<ArgumentParser, ?> function, ArgumentParser args, AtomicReference result)`
  + `setExecutable(Supplier supplier, AtomicReference result)`
  
Those methods, taking advantage of **lambda expressions**, allows you to directly **refer to a 
method** and its arguments on a single line, with the following format:

    class::methodReference
    
--------------

```java
import com.github.javinator9889.threading.threads.notifyingthread.NotifyingThread;
import com.github.javinator9889.utils.ArgumentParser;

public class LambdasExampleClass {
    private int mFirstField;
    private String mSecondField;
    private ArrayList<HashMap<String, Integer>> mThirdField;
    
    // A constructor that initializes the class fields
    // All methods used are invented and they have no implementation
    public LambdasExampleClass() {
        mFirstField = getRandomValue();
        mSecondField = getRandomString();
        mThirdField = generateListOfMaps();
    }
    
    public void runnableOperation() {
        // Operation that takes no arguments and return nothing
        System.out.println(mFirstField + mSecondField + mThirdField.toString());
    }
    
    public void consumerOperation(ArgumentParser args) {
        int valueToConsume = args.getInt("value");
        // Do something with the value, for example, assigning
        // "mFirstField" to the pow of that two values
        mFirstField = Math.pow(valueToConsume, mFirstField);
    }
    
    public String supplierOperation() {
        // Do something with the "mSecondField", for example,
        // appending current thread name
        return mSecondField + Thread.currentThread().getName();
    }
    
    public List<Map<String, Integer>> functionOperation(ArgumentParser args) {
        int firstValue = args.getInt("firstParam");
        int secondValue = args.getInt("secondParam");
        String thirdValue = args.getString("thirdParam");
        // Do something with the three params and the field
        // "mThirdField", for example, obtaining the keys
        // in which the value equals "firstValue" or "secondValue"
        // and changing it to the "thirdValue"
        List<Map<String, Integer>> result = new ArrayList(mThirdField.size());
        for (Map<String, Integer> currentVal : mThirdField) {
            if (currentVal.containsValue(firstValue)) {
                // do the interchange
            }
            if (currentVal.containsValue(secondValue)) {
                // do the interchange
            }
        }
        return result;
    }
    
    public void runFunctionsOnANewThread() {
        // First, we will generate a new NotifyingThread
        // for each function
        NotifyingThread first = new NotifyingThread();
        NotifyingThread second = new NotifyingThread();
        NotifyingThread third = new NotifyingThread();
        NotifyingThread fourth = new NotifyingThread();
        
        // Now, we need to create ArgumentParser objects
        // for our functions arguments.
        ArgumentParser consumerArgs = new ArgumentParser();
        ArgumentParser functionArgs = new ArgumentParser();
        
        // In addition, we need to create the AtomicReference
        // objects that will store the functions result
        AtomicReference<String> supplierResult = new AtomicReference<>();
        AtomicReference<List<Map<String, Integer>>> functionResult = new AtomicReference<>();
        
        // Finally, setup the params with their values
        consumerArgs.putParam("value", getRandomValue());
        functionArgs.putParam("firstParam", getRandomValue());
        functionArgs.putParam("secondParam", getRandomValue());
        functionArgs.putParam("thirdParam", getRandomString());
        
        // Now, use lambdas for setting the executables that are
        // going to be executed
        first.setExecutable(this::runnableOperation);
        second.setExecutable(this::consumerOperation, consumerArgs);
        third.setExecutable(this::supplierOperation, supplierResult);
        fourth.setExecutable(this::functionOperation, functionArgs, functionResult);
        
        // Start the threads
        first.start();
        second.start();
        third.start();
        fourth.start();
        
        // Wait for the threads to finish and work with the results
        System.out.println(supplierResult.get());
        System.out.println(functionResult.get());
    }
}
```