package com.github.javinator9889.threading.pools.rejectedhandlers;

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
 * Created by Javinator9889 on 17/11/2018 - ThreadingTools.
 */

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RunWhenTasksFinishedOnRejectedHandler implements RejectedExecutionHandler {
    private long mTimeout;
    private TimeUnit mTimeUnit;

    private RunWhenTasksFinishedOnRejectedHandler() {
    }

    public RunWhenTasksFinishedOnRejectedHandler(long timeout, @NotNull TimeUnit timeUnit) {
        mTimeout = timeout;
        mTimeUnit = timeUnit;
    }

    /**
     * Method that may be invoked by a {@link ThreadPoolExecutor} when {@link
     * ThreadPoolExecutor#execute execute} cannot accept a task.  This may occur when no more
     * threads or queue slots are available because their bounds would be exceeded, or upon shutdown
     * of the Executor.
     *
     * <p>In the absence of other alternatives, the method may throw
     * an unchecked {@link RejectedExecutionException}, which will be propagated to the caller of
     * {@code execute}.
     *
     * @param thread   the runnable task requested to be executed
     * @param executor the executor attempting to execute this task
     *
     * @throws RejectedExecutionException if there is no remedy
     */
    @Override
    public void rejectedExecution(Runnable thread, ThreadPoolExecutor executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(mTimeout, mTimeUnit);
        } catch (InterruptedException ignored) {
        } finally {
            thread.run();
        }
    }
}
