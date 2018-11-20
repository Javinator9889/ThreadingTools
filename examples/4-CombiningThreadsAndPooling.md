# Examples

## 4. Combining `NotifyingThreads` and `ThreadsPooling`

Once you saw **how `ThreadsPooling`** and **`NotifyingThread`** works, lets combine both.

This section follows a *real implementation* done at the `test` folder.

--------------------

```java
import com.github.javinator9889.utils.ArgumentParser;
import com.github.javinator9889.threading.threads.notifyingthread.OnThreadCompletedListener;
import com.github.javinator9889.threading.threads.notifyingthread.NotifyingThread;
import com.github.javinator9889.threading.pools.ThreadsPooling;


public class CombiningClass implements OnThreadCompletedListener {
    private static final String HEAVY_OPERATIONS = "heavy_operations";
    private static final String FIRST_DIVIDE = "first_divide";
    private static final String SECOND_DIVIDE = "second_divide";
    private static final String PRIME_FACTORS = "prime_factors";
    private static final int MAXF = Integer.MAX_VALUE / 20;
    private AtomicReference<Double> mHeavyOperationResult;
    private AtomicReference<List<String>> mPrimeFactorsResult;
    private HeavyLoadClass mHeavyLoadClass;
    private ThreadsPooling mThreadsPooling;
    
    public CombiningClass() {
        mHeavyOperationResult = new AtomicReference<>();
        mPrimeFactorsResult = new AtomicReference<>();
        mHeavyLoadClass = new HeavyLoadClass();
        mThreadsPooling = ThreadsPooling.builder()
                    .withQueueCapacity(10)
                    .withMaximumPoolSize(20)
                    .withKeepAliveInSeconds(1)
                    .build();
    }
    
    public void runOperations() {
        // At this method, we will create and subscribe threads
        // to the ThreadsPooling. Also, we will know whether a thread
        // has finished its execution
        
        Random generator = new Random();
        // Create the NotifyingThreads with custom names
        NotifyingThread mathOperationsThread = new NotifyingThread(HEAVY_OPERATIONS);
        NotifyingThread firstDivideBy29Thread = new NotifyingThread(FIRST_DIVIDE);
        NotifyingThread secondDivideBy29Thread = new NotifyingThread(SECOND_DIVIDE);
        NotifyingThread getFactorPrimeNumbersThread = new NotifyingThread(PRIME_FACTORS);
        
        // Create and initialize ArgumentParser classes
        // We define also the initial capacity as we know
        // how many arguments we are going to store
        ArgumentParser divisionArgs = new ArgumentParser(1);
        ArgumentParser primeFactorArgs = new ArgumentParser(1);
        
        divisionArgs.putParam("number", generator.nextLong());
        primeFactorArgs.putParam("number", generator.nextInt(MAXF));
        
        // Subscribe this class for listenting all threads completion
        mathOperationsThread.addOnThreadCompletedListener(this);
        firstDivideBy29Thread.addOnThreadCompletedListener(this);
        secondDivideBy29Thread.addOnThreadCompletedListener(this);
        getFactorPrimeNumbersThread.addOnThreadCompletedListener(this);
        
        // Assign to each thread its work, by using lambdas
        mathOperationsThread.setExecutable(mHeavyLoadClass::mathOperations, 
                                            mHeavyOperationResult);
        firstDivideBy29Thread.setExecutable(mHeavyLoadClass::isDivisibleBy29);
        secondDivideBy29Thread.setExecutable(mHeavyLoadClass::isDivisibleBy29,
                                             divisionArgs);
        getFactorPrimeNumbersThread.setExecutable(mHeavyLoadClass::primeFactors, 
                                                  primeFactorArgs,
                                                  mPrimeFactorsResult);
        
        // Add the threads to the ThreadsPooling object and start execution
        mThreadsPooling.add(mathOperationsThread,
                            firstDivideBy29Thread,
                            secondDivideBy29Thread,
                            getFactorPrimeNumbersThread);
        mThreadsPooling.start();
    }
    
    @Override
    public void onThreadCompletedListener(@NotNull Thread thread, @Nullable Throwable exception) {
        // First, we will handle the exception
        if (exception != null) {
            System.err.println("Exception thrown by thread: " + thread.getName());
            exception.printStackTrace();
        }
        // Handle finished threads
        switch (thread.getName()) {
            case FIRST_DIVIDE:
                // First division thread has finished its execution
                System.out.println("First division finished");
                break;
            case SECON_DIVIDE:
                // Second divide thread has finished its execution
                System.out.println("Second division finished");
                break;
            case HEAVY_OPERATIONS:
                // Math calculations thread has finished its execution
                // We can safely print the result
                System.out.println("Heavy operations finished");
                System.out.println("Operations result: " + mHeavyOperationResult.get());
                break;
            case PRIME_FACTORS:
                // Prime factors thread has finished its execution
                // We can safely print the result
                System.out.println("Prime factors finished");
                System.out.println("Prime factors:");
                List<String> result = mPrimeFactorsResult.get();
                // First element is a descriptor (in this case)
                String firstItem = result.remove(0);
                // Print prime factors
                System.out.println(firstItem + " | Factors: " + Arrays.toString(result.toArray()));
                break;
            default:
                System.err.println("Non contemplated case");
                break;
        }
    }

    // An example class that takes too much time to finish its working    
    public class HeavyLoadClass {
        private Random mRandom;
        private double mFirstAttribute;
        private double mSecondAttribute;
        private int mMultiplier;
        private int mSPF[] = new int[MAXF];
        
        // Default constructor, that puts all the values to a
        // random one
        public HeavyLoadClass() {
            mRandom = new Random();
            mFirstAttribute = mRandom.nextDouble();
            mSecondAttribute = mRandom.nextDouble();
            mMultiplier = mRandom.nextInt(12345678);
        }
        
        public double mathOperations() {
            // Do mathematical operations by using the 
            // class fields
            double pow = Math.pow(mFirstAttribute * mMultiplier, mSecondAttribute);
            double powTan = Math.tan(pow);
            double piPow = Math.pow(powTan, Math.PI);
            return piPow / Math.sqrt(1337);
        }
        
        public void isDivisibleBy29() {
            // Just checks if a multiplication of the class
            // fields is divisible by 29
            long number = (long) Math.floor(mFirstAttribute * mMultiplier * Math.E * 100_000_000);
            final long original = number;
            while (number / 100 > 0) {
                int lastDigit = (int) number % 10;
                number /= 10;
                number += lastDigit * 3;
            }
            System.out.println("Is " + original + " divisible by 29?: " + (number % 29 == 0));
        }
        
        public void isDivisibleBy29(ArgumentParser args) {
            // Checks if the given value is divisible by 29
            long number = args.getLong("number");
            final long original = number;
            while (number / 100 > 0) {
                int lastDigit = (int) number % 10;
                number /= 10;
                number += lastDigit * 3;
            }
            System.out.println("Is " + original + " divisible by 29?: " + (number % 29 == 0));
        }
        
        public List<String> primeFactors(ArgumentParser args) {
            // Calculate prime numbers factorization for the given value
            // It uses "sieve" method
            int number = args.getInt("number");
            List<String> result = new ArrayList<>();
            result.add("Original number: " + String.valueOf(number));
            sieve();
            while (number != 1) {
                result.add(String.valueOf(mSPF[number]));
                number /= mSPF[number];
            }
            return result;
        }
        
        // Method for calculating prime numbers
        private void sieve() {
            mSPF[1] = 1;
            IntStream.range(2, MAXF).parallel().forEach(i -> mSPF[i] = i);
            for (int i = 4; i < MAXF; i += 2)
                mSPF[i] = 2;
            for (int i = 3; i * i < MAXF; ++i)
                if (mSPF[i] == i)
                    for (int j = i * i; j < MAXF; j += i)
                        if (mSPF[j] == j)
                            mSPF[j] = i;
        }
    }
}
```