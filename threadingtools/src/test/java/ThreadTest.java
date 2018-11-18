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
 * Created by Javinator9889 on 18/11/2018 - ThreadingTools.
 */

import com.github.javinator9889.threading.threads.notifyingthread.NotifyingThread;
import com.github.javinator9889.utils.ArgumentParser;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadTest {
    private volatile int mInt;

    public ThreadTest() {
        mInt = new Random().nextInt(2345);
    }

    public void calculate() {
        System.out.println(mInt * 32 / 17);
    }

    public void calculate(ArgumentParser arguments) {
        int multiplier = arguments.getInt("mul");
        int divider = arguments.getInt("div");
        System.out.println(mInt * multiplier / divider);
    }

    public int doInt() {
        return mInt * new Random().nextInt(1337);
    }

    public int doInt(ArgumentParser argumentParser) {
        return mInt * argumentParser.getInt("mul") *
                new Random().nextInt(argumentParser.getInt("bound"));
    }

    @Test
    public void test() {
        NotifyingThread thread1 = new NotifyingThread();
        NotifyingThread thread2 = new NotifyingThread();
        NotifyingThread thread3 = new NotifyingThread();
        NotifyingThread thread4 = new NotifyingThread();
        ThreadTest t = new ThreadTest();
        System.out.println(t.mInt);
        ArgumentParser parser = new ArgumentParser(2);
        parser.putParam("mul", 12);
        parser.putParam("div", 13);
        ArgumentParser parser1 = new ArgumentParser(2);
        parser1.putParam("mul", new Random().nextInt(123));
        parser1.putParam("bound", 1457);
        AtomicReference<Integer> doIntR = new AtomicReference<>();
        AtomicReference<Integer> r2 = new AtomicReference<>();
        thread1.setExecutable(t::calculate);
        thread2.setExecutable(t::calculate, parser);
        thread3.setExecutable(t::doInt, doIntR);
        thread4.setExecutable(t::doInt, parser1, r2);
        System.out.println(parser.toString());
        System.out.println(parser1.toString());
        System.out.println();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try {
            thread3.join();
            System.out.println(doIntR.get());
            thread4.join();
            System.out.println(r2.get());
        } catch (InterruptedException ignored) {
        }
    }
}
