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

public class ThreadTest {
    private int mInt;

    public ThreadTest() {
        mInt = new Random().nextInt();
    }

    public void calculate() {
        System.out.println(mInt * 32 / 17);
    }

    public void calculate(ArgumentParser arguments) {
        int multiplier = arguments.getIntParam("mul");
        int divider = arguments.getIntParam("div");
        System.out.println(mInt * multiplier / divider);
    }

    @Test
    public void test() {
        NotifyingThread thread1 = new NotifyingThread();
        NotifyingThread thread2 = new NotifyingThread();
        ThreadTest t = new ThreadTest();
        ArgumentParser parser = new ArgumentParser(2);
        parser.putParam("mul", 12);
        parser.putParam("div", 13);
        thread1.execute(t::calculate);
        thread2.execute(t::calculate, parser);
        thread1.start();
        thread2.start();
    }
}
