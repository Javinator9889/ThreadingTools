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
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NormalPooling extends AbstractPooling implements OnThreadCompletedListener {
    @Test
    public void testIncreaseConcurrentProcess() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        pooling.updateConcurrentThreadsRunning(6);
    }

    @Test
    public void testDecreaseConcurrentProcess() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        pooling.updateConcurrentThreadsRunning(2);
    }

    @After
    public void after() throws InterruptedException {
        float random = new Random().nextFloat();
        if (random >= 0.512) {
            System.out.println("Waiting threads to finish...");
            ThreadsPooling pooling = getThreadsPooling();
            while (!pooling.shutdownWaitTermination(1, TimeUnit.MINUTES))
                System.out.println("Active threads: " + pooling.getActiveThreadsCount());
            System.out.println("Max active threads: " + pooling.getLargestActiveThreadsRunning());
            System.out.println("Completed: " + pooling.getCompletedThreadCount());
        } else {
            System.out.println("Finishing abruptly");
            List<Runnable> nonExecuted = getThreadsPooling().shutdownImmediately();
            System.out.println(Arrays.toString(nonExecuted.toArray()));
        }
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
