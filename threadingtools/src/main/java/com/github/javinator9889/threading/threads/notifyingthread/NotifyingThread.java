package com.github.javinator9889.threading.threads.notifyingthread;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NotifyingThread extends Thread implements Thread.UncaughtExceptionHandler {
    public static final int DEFAULT_CAPACITY = 1;
    public static final String THREAD_PREFIX = "NotifyingThread-";
    private static final AtomicInteger mThreadNumber = new AtomicInteger();
    private ArrayList<OnThreadCompleteListener> mSubscribedClasses;
    private AtomicBoolean mShouldCallSubscribedClassesAsynchronously = new AtomicBoolean(false);

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (null, null, gname)},
     * where {@code gname} is a newly generated name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public NotifyingThread() {
        this(null, null, THREAD_PREFIX + nextThreadNumber());
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated name. Automatically
     * generated names are of the form {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public NotifyingThread(@NotNull OnThreadCompleteListener... listeners) {
        this(null, null, THREAD_PREFIX + nextThreadNumber(), listeners);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (null, target,
     * gname)}, where {@code gname} is a newly generated name. Automatically generated names are of
     * the form {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     *
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this classes {@code run} method does nothing.
     */
    public NotifyingThread(Runnable target) {
        this(null, target, THREAD_PREFIX + nextThreadNumber());
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread
     * } {@code (null, target, gname, listeners)}, where {@code gname} is a newly generated name.
     * Automatically generated names are of the form {@code "Thread-"+}<i>n</i>, where <i>n</i> is
     * an integer.
     *
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this classes {@code run} method does nothing.
     */
    public NotifyingThread(Runnable target, @NotNull OnThreadCompleteListener... listeners) {
        this(null, target, THREAD_PREFIX + nextThreadNumber(), listeners);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (group, target,
     * gname)}, where {@code gname} is a newly generated name. Automatically generated names are of
     * the form {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     *
     * @param group  the thread group. If {@code null} and there is a security manager, the group is
     *               determined by {@linkplain SecurityManager#getThreadGroup
     *               SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *               SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *               current thread's thread group.
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target) {
//        super(group, target);
        this(group, target, THREAD_PREFIX + nextThreadNumber());
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread}
     * {@code (group, target, gname, listeners)} , where {@code gname} is a newly generated name.
     * Automatically generated names are of the form {@code "Thread-"+}<i>n</i>, where <i>n</i> is
     * an integer.
     *
     * @param group  the thread group. If {@code null} and there is a security manager, the group is
     *               determined by {@linkplain SecurityManager#getThreadGroup
     *               SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *               SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *               current thread's thread group.
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target,
                           @NotNull OnThreadCompleteListener... listeners) {
        this(group, target, THREAD_PREFIX + nextThreadNumber(), listeners);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (null, null, name)}.
     *
     * @param name the name of the new thread
     */
    public NotifyingThread(@NotNull String name) {
        this(null, null, name);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread
     * } {@code (null, null, name, listeners)}.
     *
     * @param name the name of the new thread
     */
    public NotifyingThread(@NotNull String name, @NotNull OnThreadCompleteListener... listeners) {
        this(null, null, name, listeners);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (group, null,
     * name)}.
     *
     * @param group the thread group. If {@code null} and there is a security manager, the group is
     *              determined by {@linkplain SecurityManager#getThreadGroup
     *              SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *              SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *              current thread's thread group.
     * @param name  the name of the new thread
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     */
    public NotifyingThread(@Nullable ThreadGroup group, @NotNull String name) {
        this(group, null, name);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread
     * } {@code (group, null, name, listeners)}.
     *
     * @param group the thread group. If {@code null} and there is a security manager, the group is
     *              determined by {@linkplain SecurityManager#getThreadGroup
     *              SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *              SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *              current thread's thread group.
     * @param name  the name of the new thread
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     */
    public NotifyingThread(@Nullable ThreadGroup group, @NotNull String name,
                           @NotNull OnThreadCompleteListener... listeners) {
        this(group, null, name, listeners);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String) NotifyingThread} {@code (null, target,
     * name)}.
     *
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     * @param name   the name of the new thread.
     */
    public NotifyingThread(Runnable target, String name) {
        this(null, target, name);
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
     * #NotifyingThread(ThreadGroup, Runnable, String, OnThreadCompleteListener...) NotifyingThread}
     * {@code (null, target, name, listeners)}.
     *
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     * @param name   the name of the new thread.
     */
    public NotifyingThread(Runnable target, String name,
                           @NotNull OnThreadCompleteListener... listeners) {
        this(null, target, name, listeners);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, and belongs to the thread group referred to by {@code
     * group}.
     *
     * <p>If there is a security manager, its
     * {@link SecurityManager#checkAccess(ThreadGroup) checkAccess} method is invoked with the
     * ThreadGroup as its argument.
     *
     * <p>In addition, its {@code checkPermission} method is invoked with
     * the {@code RuntimePermission("enableContextClassLoaderOverride")} permission when invoked
     * directly or indirectly by the constructor of a subclass which overrides the {@code
     * getContextClassLoader} or {@code setContextClassLoader} methods.
     *
     * <p>The priority of the newly created thread is set equal to the
     * priority of the thread creating it, that is, the currently running thread. The method
     * {@linkplain #setPriority setPriority} may be used to change the priority to a new value.
     *
     * <p>The newly created thread is initially marked as being a daemon
     * thread if and only if the thread creating it is currently marked as a daemon thread. The
     * method {@linkplain #setDaemon setDaemon} may be used to change whether or not a thread is a
     * daemon.
     *
     * @param group  the thread group. If {@code null} and there is a security manager, the group is
     *               determined by {@linkplain SecurityManager#getThreadGroup
     *               SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *               SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *               current thread's thread group.
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     * @param name   the name of the new thread
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group or cannot override the context class loader methods.
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name) {
        this(group, target, name, 0);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, and belongs to the thread group referred to by {@code
     * group}.
     *
     * <p>If there is a security manager, its
     * {@link SecurityManager#checkAccess(ThreadGroup) checkAccess} method is invoked with the
     * ThreadGroup as its argument.
     *
     * <p>In addition, its {@code checkPermission} method is invoked with
     * the {@code RuntimePermission("enableContextClassLoaderOverride")} permission when invoked
     * directly or indirectly by the constructor of a subclass which overrides the {@code
     * getContextClassLoader} or {@code setContextClassLoader} methods.
     *
     * <p>The priority of the newly created thread is set equal to the
     * priority of the thread creating it, that is, the currently running thread. The method
     * {@linkplain #setPriority setPriority} may be used to change the priority to a new value.
     *
     * <p>The newly created thread is initially marked as being a daemon
     * thread if and only if the thread creating it is currently marked as a daemon thread. The
     * method {@linkplain #setDaemon setDaemon} may be used to change whether or not a thread is a
     * daemon.
     *
     * @param group  the thread group. If {@code null} and there is a security manager, the group is
     *               determined by {@linkplain SecurityManager#getThreadGroup
     *               SecurityManager.getThreadGroup()}. If there is not a security manager or {@code
     *               SecurityManager.getThreadGroup()} returns {@code null}, the group is set to the
     *               current thread's thread group.
     * @param target the object whose {@code run} method is invoked when this thread is started. If
     *               {@code null}, this thread's run method is invoked.
     * @param name   the name of the new thread
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group or cannot override the context class loader methods.
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name,
                           @NotNull OnThreadCompleteListener... listeners) {
        this(group, target, name, 0, listeners);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, and belongs to the thread group referred to by {@code
     * group}, and has the specified <i>stack size</i>.
     *
     * <p>This constructor is identical to {@link
     * #Thread#Thread(ThreadGroup, Runnable, String)} with the exception of the fact that it allows
     * the thread stack size to be specified.  The stack size is the approximate number of bytes of
     * address space that the virtual machine is to allocate for this thread's stack.  <b>The effect
     * of the {@code stackSize} parameter, if any, is highly platform dependent.</b>
     *
     * <p>On some platforms, specifying a higher value for the
     * {@code stackSize} parameter may allow a thread to achieve greater recursion depth before
     * throwing a {@link StackOverflowError}. Similarly, specifying a lower value may allow a
     * greater number of threads to exist concurrently without throwing an {@link OutOfMemoryError}
     * (or other internal error).  The details of the relationship between the value of the {@code
     * stackSize} parameter and the maximum recursion depth and concurrency level are
     * platform-dependent.  <b>On some platforms, the value of the {@code stackSize} parameter may
     * have no effect whatsoever.</b>
     *
     * <p>The virtual machine is free to treat the {@code stackSize}
     * parameter as a suggestion.  If the specified value is unreasonably low for the platform, the
     * virtual machine may instead use some platform-specific minimum value; if the specified value
     * is unreasonably high, the virtual machine may instead use some platform-specific maximum.
     * Likewise, the virtual machine is free to round the specified value up or down as it sees fit
     * (or to ignore it completely).
     *
     * <p>Specifying a value of zero for the {@code stackSize} parameter will
     * cause this constructor to behave exactly like the {@code Thread(ThreadGroup, Runnable,
     * String)} constructor.
     *
     * <p><i>Due to the platform-dependent nature of the behavior of this
     * constructor, extreme care should be exercised in its use. The thread stack size necessary to
     * perform a given computation will likely vary from one JRE implementation to another.  In
     * light of this variation, careful tuning of the stack size parameter may be required, and the
     * tuning may need to be repeated for each JRE implementation on which an application is to
     * run.</i>
     *
     * <p>Implementation note: Java platform implementers are encouraged to
     * document their implementation's behavior with respect to the {@code stackSize} parameter.
     *
     * @param group     the thread group. If {@code null} and there is a security manager, the group
     *                  is determined by {@linkplain SecurityManager#getThreadGroup
     *                  SecurityManager.getThreadGroup()}. If there is not a security manager or
     *                  {@code SecurityManager.getThreadGroup()} returns {@code null}, the group is
     *                  set to the current thread's thread group.
     * @param target    the object whose {@code run} method is invoked when this thread is started.
     *                  If {@code null}, this thread's run method is invoked.
     * @param name      the name of the new thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this
     *                  parameter is to be ignored.
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     * @since 1.4
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name, long stackSize) {
        super(group, target, name, stackSize);
        mSubscribedClasses = new ArrayList<>(DEFAULT_CAPACITY);
        setUncaughtExceptionHandler(this);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, and belongs to the thread group referred to by {@code
     * group}, and has the specified <i>stack size</i>.
     *
     * <p>This constructor is identical to {@link
     * #Thread#Thread(ThreadGroup, Runnable, String)} with the exception of the fact that it allows
     * the thread stack size to be specified.  The stack size is the approximate number of bytes of
     * address space that the virtual machine is to allocate for this thread's stack.  <b>The effect
     * of the {@code stackSize} parameter, if any, is highly platform dependent.</b>
     *
     * <p>On some platforms, specifying a higher value for the
     * {@code stackSize} parameter may allow a thread to achieve greater recursion depth before
     * throwing a {@link StackOverflowError}. Similarly, specifying a lower value may allow a
     * greater number of threads to exist concurrently without throwing an {@link OutOfMemoryError}
     * (or other internal error).  The details of the relationship between the value of the {@code
     * stackSize} parameter and the maximum recursion depth and concurrency level are
     * platform-dependent.  <b>On some platforms, the value of the {@code stackSize} parameter may
     * have no effect whatsoever.</b>
     *
     * <p>The virtual machine is free to treat the {@code stackSize}
     * parameter as a suggestion.  If the specified value is unreasonably low for the platform, the
     * virtual machine may instead use some platform-specific minimum value; if the specified value
     * is unreasonably high, the virtual machine may instead use some platform-specific maximum.
     * Likewise, the virtual machine is free to round the specified value up or down as it sees fit
     * (or to ignore it completely).
     *
     * <p>Specifying a value of zero for the {@code stackSize} parameter will
     * cause this constructor to behave exactly like the {@code Thread(ThreadGroup, Runnable,
     * String)} constructor.
     *
     * <p><i>Due to the platform-dependent nature of the behavior of this
     * constructor, extreme care should be exercised in its use. The thread stack size necessary to
     * perform a given computation will likely vary from one JRE implementation to another.  In
     * light of this variation, careful tuning of the stack size parameter may be required, and the
     * tuning may need to be repeated for each JRE implementation on which an application is to
     * run.</i>
     *
     * <p>Implementation note: Java platform implementers are encouraged to
     * document their implementation's behavior with respect to the {@code stackSize} parameter.
     *
     * @param group     the thread group. If {@code null} and there is a security manager, the group
     *                  is determined by {@linkplain SecurityManager#getThreadGroup
     *                  SecurityManager.getThreadGroup()}. If there is not a security manager or
     *                  {@code SecurityManager.getThreadGroup()} returns {@code null}, the group is
     *                  set to the current thread's thread group.
     * @param target    the object whose {@code run} method is invoked when this thread is started.
     *                  If {@code null}, this thread's run method is invoked.
     * @param name      the name of the new thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this
     *                  parameter is to be ignored.
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     * @since 1.4
     */
    public NotifyingThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name,
                           long stackSize, @NotNull OnThreadCompleteListener... listeners) {
        super(group, target, name, stackSize);
        mSubscribedClasses = new ArrayList<>(Arrays.asList(listeners));
        setUncaughtExceptionHandler(this);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, belongs to the thread group referred to by {@code
     * group}, has the specified {@code stackSize}, and inherits initial values for {@linkplain
     * InheritableThreadLocal inheritable thread-local} variables if {@code inheritThreadLocals} is
     * {@code true}.
     *
     * <p> This constructor is identical to {@link
     * #Thread#Thread(ThreadGroup, Runnable, String, long)} with the added ability to suppress, or
     * not, the inheriting of initial values for inheritable thread-local variables from the
     * constructing thread. This allows for finer grain control over inheritable thread-locals. Care
     * must be taken when passing a value of {@code false} for {@code inheritThreadLocals}, as it
     * may lead to unexpected behavior if the new thread executes code that expects a specific
     * thread-local value to be inherited.
     *
     * <p> Specifying a value of {@code true} for the {@code inheritThreadLocals}
     * parameter will cause this constructor to behave exactly like the {@code Thread(ThreadGroup,
     * Runnable, String, long)} constructor.
     *
     * @param group               the thread group. If {@code null} and there is a security manager,
     *                            the group is determined by {@linkplain SecurityManager#getThreadGroup
     *                            SecurityManager.getThreadGroup()}. If there is not a security
     *                            manager or {@code SecurityManager.getThreadGroup()} returns {@code
     *                            null}, the group is set to the current thread's thread group.
     * @param target              the object whose {@code run} method is invoked when this thread is
     *                            started. If {@code null}, this thread's run method is invoked.
     * @param name                the name of the new thread
     * @param stackSize           the desired stack size for the new thread, or zero to indicate
     *                            that this parameter is to be ignored
     * @param inheritThreadLocals if {@code true}, inherit initial values for inheritable
     *                            thread-locals from the constructing thread, otherwise no initial
     *                            values are inherited
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     * @since 9
     */
    public NotifyingThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
        mSubscribedClasses = new ArrayList<>(DEFAULT_CAPACITY);
        setUncaughtExceptionHandler(this);
    }

    /**
     * Allocates a new {@code Thread} object so that it has {@code target} as its run object, has
     * the specified {@code name} as its name, belongs to the thread group referred to by {@code
     * group}, has the specified {@code stackSize}, and inherits initial values for {@linkplain
     * InheritableThreadLocal inheritable thread-local} variables if {@code inheritThreadLocals} is
     * {@code true}.
     *
     * <p> This constructor is identical to {@link
     * #Thread#Thread(ThreadGroup, Runnable, String, long)} with the added ability to suppress, or
     * not, the inheriting of initial values for inheritable thread-local variables from the
     * constructing thread. This allows for finer grain control over inheritable thread-locals. Care
     * must be taken when passing a value of {@code false} for {@code inheritThreadLocals}, as it
     * may lead to unexpected behavior if the new thread executes code that expects a specific
     * thread-local value to be inherited.
     *
     * <p> Specifying a value of {@code true} for the {@code inheritThreadLocals}
     * parameter will cause this constructor to behave exactly like the {@code Thread(ThreadGroup,
     * Runnable, String, long)} constructor.
     *
     * @param group               the thread group. If {@code null} and there is a security manager,
     *                            the group is determined by {@linkplain SecurityManager#getThreadGroup
     *                            SecurityManager.getThreadGroup()}. If there is not a security
     *                            manager or {@code SecurityManager.getThreadGroup()} returns {@code
     *                            null}, the group is set to the current thread's thread group.
     * @param target              the object whose {@code run} method is invoked when this thread is
     *                            started. If {@code null}, this thread's run method is invoked.
     * @param name                the name of the new thread
     * @param stackSize           the desired stack size for the new thread, or zero to indicate
     *                            that this parameter is to be ignored
     * @param inheritThreadLocals if {@code true}, inherit initial values for inheritable
     *                            thread-locals from the constructing thread, otherwise no initial
     *                            values are inherited
     *
     * @throws SecurityException if the current thread cannot create a thread in the specified
     *                           thread group
     * @since 9
     */
    public NotifyingThread(ThreadGroup group, Runnable target, String name, long stackSize,
                           boolean inheritThreadLocals,
                           @NotNull OnThreadCompleteListener... listeners) {
        super(group, target, name, stackSize, inheritThreadLocals);
        mSubscribedClasses = new ArrayList<>(Arrays.asList(listeners));
        setUncaughtExceptionHandler(this);
    }

    private static synchronized int nextThreadNumber() {
        return mThreadNumber.getAndIncrement();
    }

    /**
     * Method invoked when the given thread terminates due to the given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param thread    the thread
     * @param exception the exception
     */
    @Override
    public void uncaughtException(final @NotNull Thread thread,
                                  final @NotNull Throwable exception) {
        callSubscribedClasses(thread, exception);
    }

    private void callSubscribedClasses(final @NotNull Runnable thread,
                                       final @NotNull Throwable exception) {
        if (mShouldCallSubscribedClassesAsynchronously.get())
            for (final OnThreadCompleteListener subscribedClass : mSubscribedClasses)
                new Thread(() -> subscribedClass.onThreadCompleteListener(thread, exception)).run();
        else
            for (OnThreadCompleteListener subscribedClass : mSubscribedClasses)
                subscribedClass.onThreadCompleteListener(thread, exception);
    }
}
