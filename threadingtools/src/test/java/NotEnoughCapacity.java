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

import com.github.javinator9889.threading.errors.NoRejectedHandlerError;
import com.github.javinator9889.threading.pools.ThreadsPooling;
import com.github.javinator9889.threading.threads.notifyingthread.NotifyingThread;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NotEnoughCapacity extends AbstractPooling {
    @Test
    @Override
    public void test() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        try {
            for (int i = 10; i < 20; ++i)
                pooling.add(new NotifyingThread(target(i)));
        } catch (NoRejectedHandlerError error) {
            System.err.println("Not enough capacity");
            error.printStackTrace();
        }
    }

    @Test
    public void testNoAction() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        pooling.updateRejectedExecutionHandler(ThreadsPooling.NO_ACTION_ON_REJECTED_HANDLER);
        for (int i = 10; i < 20; ++i)
            pooling.add(new NotifyingThread(target(i)));
    }

    @Test
    public void testImmediatelyRun() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        pooling.updateRejectedExecutionHandler(ThreadsPooling.IMMEDIATELY_RUN_ON_REJECTED_HANDLER);
        for (int i = 10; i < 20; ++i)
            pooling.add(new NotifyingThread(target(i)));
    }

    @Test
    public void testWaitAndRun() {
        super.test();
        ThreadsPooling pooling = getThreadsPooling();
        pooling.updateRejectedExecutionHandler(ThreadsPooling.WAIT_SHUTDOWN_RUN_TASK_ON_REJECTED_HANDLER);
        for (int i = 10; i < 20; ++i)
            pooling.add(new NotifyingThread(target(i))); // Only the first one will be executed
        // as we request the executor shutdown, so no more process are admitted.
    }

    @After
    public void after() throws InterruptedException {
        getThreadsPooling().shutdownWaitTermination(1, TimeUnit.MINUTES);
    }
}
