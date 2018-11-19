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
import com.github.javinator9889.threading.threads.notifyingthread.OnThreadCompletedListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPooling implements OnThreadCompletedListener {
    private ThreadsPooling mThreadsPooling;

    @Before
    public void before() {
        mThreadsPooling = ThreadsPooling.builder()
                .withQueueCapacity(10)
                .withMaximumPoolSize(20)
                .withKeepAliveInSeconds(1)
                .build();
    }

    protected Runnable target(int value) {
        return () -> {
            int timeout = new Random().nextInt(500);
            try {
                System.out.printf("Thread %d has timeout: %d\n", value, timeout);
                TimeUnit.SECONDS.sleep(1);
                for (int i = 0; i < 10; ++i) {
                    System.out.printf("I am the %d thread\n", value);
                    TimeUnit.MILLISECONDS.sleep(timeout);
                }
            } catch (InterruptedException ignored) {
                System.err.printf("Thread %d interrupted\n", value);
            }
        };
    }

    @Test
    public void test() {
        for (int i = 0; i < 10; ++i) {
            NotifyingThread currentThread = new NotifyingThread(target(i));
            currentThread.addOnThreadCompletedListener(this);
            mThreadsPooling.add(currentThread);
        }
        mThreadsPooling.start();
    }

    public ThreadsPooling getThreadsPooling() {
        return mThreadsPooling;
    }

    /**
     * When a thread finish its execution, if using a {@link NotifyingThread} and the class is
     * subscribed, this method is called, with the {@code Runnable} which corresponds the just
     * finished thread, and the {@code Throwable} containing the exception (if any exception has
     * benn thrown).
     * <p>
     * Refer to {@link NotifyingThread#addOnThreadCompletedListener(OnThreadCompletedListener)} for
     * getting more information about subscribing classes.
     *
     * @param thread    the thread that has just finished its execution.
     * @param exception the exception if happened, else {@code null}.
     */
    @Override
    public void onThreadCompletedListener(@NotNull Thread thread, @Nullable Throwable exception) {
        System.out.println("Finished!");
        if (exception != null) {
            System.err.println("There is an exception thrown: " + exception.getMessage());
        }
        System.out.println(thread.toString());
    }
}
