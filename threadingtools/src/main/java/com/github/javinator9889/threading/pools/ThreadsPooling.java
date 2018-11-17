package com.github.javinator9889.threading.pools;

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
 * Created by Javinator9889 on 15/11/2018 - ThreadingTools.
 */

import com.github.javinator9889.threading.pools.rejectedhandlers.DefaultRejectedExecutionHandler;
import com.github.javinator9889.threading.pools.rejectedhandlers.NoRejectedExecutionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ThreadsPooling {
    public static final int DEFAULT_CORE_THREADS = 4;
    public static final int DEFAULT_MAX_THREADS = 8;
    public static final long DEFAULT_KEEP_ALIVE = 100;
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
    public static final int DEFAULT_QUEUE_CAPACITY = 100;
    public static final DefaultRejectedExecutionHandler DEFAULT_REJECTED_EXECUTION_HANDLER =
            new DefaultRejectedExecutionHandler();
    public static final NoRejectedExecutionHandler NO_ACTION_ON_REJECTED_HANDLER =
            new NoRejectedExecutionHandler();

    private ThreadPoolExecutor mPoolExecutor;
    private BlockingQueue<Runnable> mWorkingThreadsQueue;
    private RejectedExecutionHandler mRejectedExecutionHandler;

    private ThreadsPooling(int coreThreads, int maximumPoolSize, long keepAliveTime,
                           TimeUnit timeUnit, BlockingQueue<Runnable> workingThreadsQueue) {
        this(coreThreads,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workingThreadsQueue,
                DEFAULT_REJECTED_EXECUTION_HANDLER);
    }

    private ThreadsPooling(int coreThreads, int maximumPoolSize, long keepAliveTime,
                           TimeUnit timeUnit, BlockingQueue<Runnable> workingThreadsQueue,
                           ThreadFactory factory) {
        this(coreThreads,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workingThreadsQueue,
                factory,
                DEFAULT_REJECTED_EXECUTION_HANDLER);
    }

    private ThreadsPooling(int coreThreads, int maximumPoolSize, long keepAliveTime,
                           TimeUnit timeUnit, BlockingQueue<Runnable> workingThreadsQueue,
                           RejectedExecutionHandler rejectedExecutionHandler) {
        mWorkingThreadsQueue = workingThreadsQueue;
        mRejectedExecutionHandler = rejectedExecutionHandler;
        mPoolExecutor = new ThreadPoolExecutor(coreThreads,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workingThreadsQueue,
                rejectedExecutionHandler);
    }

    private ThreadsPooling(int coreThreads, int maximumPoolSize, long keepAliveTime,
                           TimeUnit timeUnit, BlockingQueue<Runnable> workingThreadsQueue,
                           ThreadFactory factory,
                           RejectedExecutionHandler rejectedExecutionHandler) {
        mWorkingThreadsQueue = workingThreadsQueue;
        mRejectedExecutionHandler = rejectedExecutionHandler;
        mPoolExecutor = new ThreadPoolExecutor(coreThreads,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workingThreadsQueue,
                factory,
                rejectedExecutionHandler);
    }

    public void add(@NotNull Runnable thread) {
        try {
            mWorkingThreadsQueue.add(thread);
        } catch (IllegalStateException | ClassCastException | NullPointerException |
                IllegalArgumentException ignored) {
            mRejectedExecutionHandler.rejectedExecution(thread, mPoolExecutor);
        }
    }

    public void add(@NotNull Runnable... threads) {
        int sizeBeforeAddingTheElements = mWorkingThreadsQueue.size();
        int remainingCapacity = mWorkingThreadsQueue.remainingCapacity();
        try {
            mWorkingThreadsQueue.addAll(Arrays.asList(threads));
        } catch (IllegalStateException | ClassCastException | NullPointerException |
                IllegalArgumentException | UnsupportedOperationException ignored) {
            int threadNotAdded = remainingCapacity - sizeBeforeAddingTheElements;
            if (threadNotAdded >= threads.length)
                threadNotAdded = 0;
            mRejectedExecutionHandler.rejectedExecution(threads[threadNotAdded], mPoolExecutor);
        }
    }

    public int start() {
        return mPoolExecutor.prestartAllCoreThreads();
    }

    public boolean shutdownWaitTermination() throws InterruptedException {
        return shutdownWaitTermination(100, TimeUnit.MILLISECONDS);
    }

    public boolean shutdownWaitTermination(long timeout, @NotNull TimeUnit waitingUnit)
            throws InterruptedException {
        mPoolExecutor.shutdown();
        return mPoolExecutor.awaitTermination(timeout, waitingUnit);
    }

    public void shutdownNotWaiting() {
        mPoolExecutor.shutdown();
    }

    public List<Runnable> shutdownImmediately() {
        return mPoolExecutor.shutdownNow();
    }

    public void updateConcurrentThreadsRunning(int newAmountOfThreadsRunning) {
        if (newAmountOfThreadsRunning < 0)
            return;
        else if (newAmountOfThreadsRunning == 0)
            newAmountOfThreadsRunning = DEFAULT_CORE_THREADS;
        if (newAmountOfThreadsRunning != mPoolExecutor.getCorePoolSize())
            mPoolExecutor.setCorePoolSize(newAmountOfThreadsRunning);
    }

    public void updateMaximumActiveThreads(int newMaximumActiveThreads) {
        if (newMaximumActiveThreads < 0)
            return;
        else if (newMaximumActiveThreads == 0)
            newMaximumActiveThreads = DEFAULT_MAX_THREADS;
        if (newMaximumActiveThreads != mPoolExecutor.getMaximumPoolSize())
            mPoolExecutor.setMaximumPoolSize(newMaximumActiveThreads);
    }

    public void updateKeepAliveTime(long newKeepAliveTime) {
        updateKeepAliveTime(newKeepAliveTime, DEFAULT_TIME_UNIT);
    }

    public void updateKeepAliveTime(long newKeepAliveTime, @NotNull TimeUnit newTimeUnit) {
        mPoolExecutor.setKeepAliveTime(newKeepAliveTime, newTimeUnit);
    }

    public void updateRejectedExecutionHandler(@Nullable RejectedExecutionHandler newHandler) {
        if (newHandler == null)
            mPoolExecutor.setRejectedExecutionHandler(DEFAULT_REJECTED_EXECUTION_HANDLER);
        else
            mPoolExecutor.setRejectedExecutionHandler(newHandler);
    }

    public void updateThreadFactory(@NotNull ThreadFactory newThreadFactory) {
        mPoolExecutor.setThreadFactory(newThreadFactory);
    }

    public int getConcurrentThreadsRunning() {
        return mPoolExecutor.getCorePoolSize();
    }

    public int getMaximumThreadsRunning() {
        return mPoolExecutor.getMaximumPoolSize();
    }

    public int getActiveThreadsCount() {
        return mPoolExecutor.getActiveCount();
    }

    public long getKeepAliveTime() {
        return getKeepAliveTimeWithUnit(DEFAULT_TIME_UNIT);
    }

    public long getKeepAliveTimeWithUnit(@NotNull TimeUnit unit) {
        return mPoolExecutor.getKeepAliveTime(unit);
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return mPoolExecutor.getRejectedExecutionHandler();
    }

    public ThreadFactory getThreadFactory() {
        return mPoolExecutor.getThreadFactory();
    }

    public Builder builder() {
        return new Builder();
    }

    public final class Builder {
        private int mCoreThreads;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;
        private TimeUnit mTimeUnit;
        private int mQueueCapacity;
        private BlockingQueue<Runnable> mWorkingThreadsQueue;
        private ThreadFactory mThreadFactory;
        private RejectedExecutionHandler mRejectedExecutionHandler;

        private Builder() {
            mCoreThreads = DEFAULT_CORE_THREADS;
            mMaximumPoolSize = DEFAULT_MAX_THREADS;
            mKeepAliveTime = DEFAULT_KEEP_ALIVE;
            mTimeUnit = DEFAULT_TIME_UNIT;
            mQueueCapacity = DEFAULT_QUEUE_CAPACITY;
            mWorkingThreadsQueue = null;
            mThreadFactory = null;
            mRejectedExecutionHandler = null;
        }

        public Builder withConcurrentThreadsRunning(int concurrentThreadsRunning) {
            if (isParamHigherThanZero(concurrentThreadsRunning)) {
                mCoreThreads = (concurrentThreadsRunning == 0) ?
                        DEFAULT_CORE_THREADS :
                        concurrentThreadsRunning;
                return this;
            } else
                throw illegalArgumentException("Concurrent threads", concurrentThreadsRunning);
        }

        public Builder withMaximumPoolSize(int maximumPoolSize) {
            if (isParamHigherThanZero(maximumPoolSize)) {
                mMaximumPoolSize = (maximumPoolSize == 0) ?
                        DEFAULT_MAX_THREADS :
                        maximumPoolSize;
                return this;
            } else
                throw illegalArgumentException("Maximum threads", maximumPoolSize);
        }

        public Builder withKeepAliveInSeconds(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.SECONDS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInMiliseconds(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.MILLISECONDS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInMicroseconds(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.MICROSECONDS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInNanoseconds(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.MICROSECONDS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInMinutes(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.MINUTES;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInHours(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.HOURS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withKeepAliveInDays(long keepAliveTime) {
            if (isParamHigherThanZero(keepAliveTime)) {
                setKeepAliveTime(keepAliveTime);
                mTimeUnit = TimeUnit.DAYS;
                return this;
            } else
                throw illegalArgumentException("Keep Alive time", keepAliveTime);
        }

        public Builder withTimeUnit(@Nullable TimeUnit timeUnit) {
            mTimeUnit = (timeUnit == null) ?
                    DEFAULT_TIME_UNIT :
                    timeUnit;
            return this;
        }

        public Builder withQueueCapacity(int queueCapacity) {
            if (isParamHigherThanZero(queueCapacity)) {
                mQueueCapacity = (queueCapacity == 0) ?
                        DEFAULT_QUEUE_CAPACITY :
                        queueCapacity;
                return this;
            } else
                throw illegalArgumentException("Queue capacity", queueCapacity);
        }

        public Builder withThread(@NotNull Runnable thread) {
            if (mWorkingThreadsQueue == null)
                mWorkingThreadsQueue = new LinkedBlockingQueue<>(mQueueCapacity);
            mWorkingThreadsQueue.add(thread);
            return this;
        }

        public Builder withThreads(@NotNull Runnable... threads) {
            if (mWorkingThreadsQueue == null)
                mWorkingThreadsQueue = new LinkedBlockingQueue<>(mQueueCapacity);
            mWorkingThreadsQueue.addAll(Arrays.asList(threads));
            return this;
        }

        public Builder withThreadFactory(@Nullable ThreadFactory threadFactory) {
            mThreadFactory = threadFactory;
            return this;
        }

        public Builder withRejectedExecutionHandler(@Nullable RejectedExecutionHandler handler) {
            mRejectedExecutionHandler = handler;
            return this;
        }

        public ThreadsPooling build() {
            if (mWorkingThreadsQueue == null)
                mWorkingThreadsQueue = new LinkedBlockingQueue<>(mQueueCapacity);
            if (mThreadFactory == null && mRejectedExecutionHandler == null)
                return new ThreadsPooling(mCoreThreads,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        mTimeUnit,
                        mWorkingThreadsQueue);
            else if (mThreadFactory != null && mRejectedExecutionHandler == null)
                return new ThreadsPooling(mCoreThreads,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        mTimeUnit,
                        mWorkingThreadsQueue,
                        mThreadFactory);
            else if (mThreadFactory == null && mRejectedExecutionHandler != null)
                return new ThreadsPooling(mCoreThreads,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        mTimeUnit,
                        mWorkingThreadsQueue,
                        mRejectedExecutionHandler);
            else
                return new ThreadsPooling(mCoreThreads,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        mTimeUnit,
                        mWorkingThreadsQueue,
                        mThreadFactory,
                        mRejectedExecutionHandler);
        }

        private boolean isParamHigherThanZero(long paramToCheck) {
            return paramToCheck >= 0;
        }

        private void setKeepAliveTime(long keepAliveTime) {
            mKeepAliveTime = (keepAliveTime == 0) ?
                    DEFAULT_KEEP_ALIVE :
                    keepAliveTime;
        }

        private IllegalArgumentException illegalArgumentException(String valueName,
                                                                  long valueContent) {
            return new IllegalArgumentException(String.format("%s time must be zero or" +
                    " higher, not '%d'", valueName, valueContent));
        }
    }
}
