/*
 * Copyright © 2018 - present | ThreadingTools by Javinator9889
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 *
 * Created by Javinator9889 on 19/11/2018 - ThreadingTools.
 */

import com.github.javinator9889.threading.pools.ThreadsPooling;
import com.github.javinator9889.threading.threads.notifyingthread.NotifyingThread;
import com.github.javinator9889.utils.ArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class LambdasPooling extends AbstractPooling {
    private static final String HEAVY_OPERATIONS = "heavy_operations";
    private static final String FIRST_DIVIDE = "first_divide";
    private static final String SECON_DIVIDE = "secon_divide";
    private static final String PRIME_FACTORS = "prime_factors";
    private static final int MAXF = Integer.MAX_VALUE / 10;
    private AtomicReference<Double> mHeavyOperationResult;
    private AtomicReference<List<String>> mPrimeFactorsResult;
    private HeavyLoadClass mHeavyLoadClass;

    @Override
    public void before() {
        super.before();
        mHeavyOperationResult = new AtomicReference<>();
        mPrimeFactorsResult = new AtomicReference<>();
        mHeavyLoadClass = new HeavyLoadClass();
    }

    @Test
    @Override
    public void test() {
        ThreadsPooling pooling = getThreadsPooling();
        NotifyingThread heavyOperationsThread = new NotifyingThread(HEAVY_OPERATIONS);
        NotifyingThread firstDivideBy29Thread = new NotifyingThread(FIRST_DIVIDE);
        NotifyingThread seconDivideBy29Thread = new NotifyingThread(SECON_DIVIDE);
        NotifyingThread factorPrimeNumbThread = new NotifyingThread(PRIME_FACTORS);

        ArgumentParser divisionArgs = new ArgumentParser(1);
        ArgumentParser primeFactorsArgs = new ArgumentParser(1);
        divisionArgs.putParam("number", 1234598531L);
        primeFactorsArgs.putParam("number", new Random().nextInt(MAXF));

        heavyOperationsThread.addOnThreadCompletedListener(this);
        firstDivideBy29Thread.addOnThreadCompletedListener(this);
        seconDivideBy29Thread.addOnThreadCompletedListener(this);
        factorPrimeNumbThread.addOnThreadCompletedListener(this);

        heavyOperationsThread.setExecutable(mHeavyLoadClass::heavyOperation,
                mHeavyOperationResult);
        firstDivideBy29Thread.setExecutable(mHeavyLoadClass::isDivisibleBy29);
        seconDivideBy29Thread.setExecutable(mHeavyLoadClass::isDivisibleBy29,
                divisionArgs);
        factorPrimeNumbThread.setExecutable(mHeavyLoadClass::primeFactors,
                primeFactorsArgs,
                mPrimeFactorsResult);

        pooling.add(heavyOperationsThread,
                firstDivideBy29Thread,
                seconDivideBy29Thread,
                factorPrimeNumbThread);
        pooling.start();
    }

    @After
    public void after() throws InterruptedException {
        getThreadsPooling().shutdownWaitTermination(10, TimeUnit.MINUTES);
    }

    @Override
    public void onThreadCompletedListener(@NotNull Thread thread, @Nullable Throwable exception) {
        if (exception != null) {
            System.err.println("Exception thrown by thread: " + thread.getName());
            exception.printStackTrace();
        }
        switch (thread.getName()) {
            case FIRST_DIVIDE:
                System.out.println("First division finished");
                break;
            case SECON_DIVIDE:
                System.out.println("Second division finished");
                break;
            case HEAVY_OPERATIONS:
                System.out.println("Heavy operations finished");
                System.out.println("Operations result: " + mHeavyOperationResult.get());
                break;
            case PRIME_FACTORS:
                System.out.println("Prime factors finished");
                System.out.println("Prime factors:");
                List<String> result = mPrimeFactorsResult.get();
                String firstItem = result.remove(0);
                System.out.println(firstItem + " | Factors: " + Arrays.toString(result.toArray()));
                break;
            default:
                System.err.println("Non contemplated case");
                break;
        }
    }

    public class HeavyLoadClass {
        private Random mRandom;
        private double mFirstAttribute;
        private double mSecondAttribute;
        private long mPrime;
        private int mMultiplier;
        private int mSPF[] = new int[MAXF];

        public HeavyLoadClass() {
            mRandom = new Random();
            mFirstAttribute = mRandom.nextDouble();
            mSecondAttribute = mRandom.nextDouble();
            mPrime = mRandom.nextLong();
            mMultiplier = mRandom.nextInt(123456);
        }

        public double heavyOperation() {
            double result = Math.pow(mFirstAttribute * mMultiplier, mSecondAttribute);
            double tanh = Math.tanh(result);
            double piExp = Math.pow(tanh, Math.PI);
            return piExp / 1337;
        }

        public void isDivisibleBy29() {
            long number = (long) Math.floor(Math.PI + Math.E * 100000000);
            final long org = number;
            while (number / 100 > 0) {

                int last_digit = (int) number % 10;
                number /= 10;
                number += last_digit * 3;
            }

            System.out.println("Is " + org + " divisible by 29?: " + (number % 29 == 0));
        }

        public void isDivisibleBy29(ArgumentParser args) {
            long number = (long) Math.pow(args.getLong("number") * Math.E, Math.PI);
            final long org = number;
            while (number / 100 > 0) {

                int last_digit = (int) number % 10;
                number /= 10;
                number += last_digit * 3;
            }
            System.out.println("Is " + org + " divisible by 29?: " + (number % 29 == 0));
        }

        public List<String> primeFactors(ArgumentParser args) {
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
