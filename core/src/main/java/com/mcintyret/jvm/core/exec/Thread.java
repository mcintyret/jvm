package com.mcintyret.jvm.core.exec;

import com.google.common.collect.Iterables;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.util.Utils;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.mcintyret.jvm.load.ClassLoader.getClassLoader;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public final class Thread {

    private final OopClass thisThread;

    private final java.lang.Thread thread;

    private volatile boolean interrupted;

    private final Deque<ExecutionStack> executionStacks = new ArrayDeque<>();

    private ExecutionStack currentStack;

    public Thread(OopClass thisThread, OopClass name) {
        this.thisThread = thisThread;
        this.thread = new ActualThread(Utils.toString(name));
    }

    // For system threads. Note these must call Heap.register manually!
    public Thread(OopClass thisThread, java.lang.Thread thread) {
        this.thisThread = thisThread;
        this.thread = thread;
    }

    public void start() {
        thread.start();
    }

    public OopClass getThisThread() {
        return thisThread;
    }

    public void interrupt() {
        if (thread.isAlive()) {
            interrupted = true;
            thread.interrupt();
        }
    }

    public void sleep(long millis) throws InterruptedException {
        if (thread != java.lang.Thread.currentThread()) {
            throw new AssertionError("Cannot call Thread.sleep from a different thread!");
        }
        try {
            java.lang.Thread.sleep(millis);
        } catch (InterruptedException e) {
            interrupted = false;
            throw e;
        }
    }

    public boolean isInterrupted(boolean clear) {
        boolean ret = interrupted;
        if (clear) {
            interrupted = false;
        }
        return ret;
    }

    public java.lang.Thread getThread() {
        return thread;
    }

    public ExecutionStack getCurrentStack() {
        return currentStack;
    }

    public NativeReturn execute(Method method, Variables args) {
        try {
            return doExecute(method, args);
        } catch (Throwable t) {
            String threadName = thread == null ? "main" : thread.getName();
            System.out.println("ERROR on thread '" + threadName + "': " + t.getMessage());
            System.out.println("Caused By:");
            executionStacks.forEach(stack -> {
                stack.getStack().forEach(exec -> {
                    System.out.println("\t" + exec.getMethod());
                });
            });

            throw t;
        }
    }

    public int getTotalStackDepth() {
        int total = 0;
        for (ExecutionStack stack : executionStacks) {
            total += stack.getStack().size();
        }
        return total;
    }

    private NativeReturn doExecute(Method method, Variables args) {
        if (thread != null && java.lang.Thread.currentThread() != thread) {
            throw new IllegalStateException();
        }

        currentStack = new ExecutionStack();
        executionStacks.push(currentStack);
        currentStack.push(makeExecution(method, args));

        NativeReturn ret = currentStack.execute();

        if (currentStack != executionStacks.pop()) {
            throw new IllegalStateException();
        }

        currentStack = executionStacks.peek();

        return ret;
    }

    public Iterable<Execution> getExecutions() {
        return Iterables.concat(executionStacks);
    }

    private Execution makeExecution(Method method, Variables args) {
        return new Execution(method, args, this);
    }

    public class ActualThread extends java.lang.Thread {

        private final ClassObject THREAD_CLASS = getClassLoader().getClassObject("java/lang/Thread");

        private final Method THREAD_RUN = THREAD_CLASS.findMethod("run", "()V", false);

        private final Field THREAD_DAEMON = THREAD_CLASS.findField("daemon", false);

        public ActualThread(String name) {
            super(name);
        }

        @Override
        public synchronized void start() {
            boolean isDaemon = THREAD_DAEMON.getInt(thisThread) != 0;
            thread.setDaemon(isDaemon);

            super.start();
        }

        @Override
        public void run() {
            Heap.registerThread();
            try {

                Method method = THREAD_RUN;
                // Find the overridden Thread.run() method, if there is one
                if (thisThread.getClassObject() != THREAD_CLASS) {
                    method = thisThread.getClassObject().getInstanceMethods()[THREAD_RUN.getOffset()];
                }

                Variables args = method.newArgArray(thisThread);
                execute(method, args);

            } finally {
                Threads.deregister(Thread.this);
                Heap.deregisterThread();
            }
        }

        public Thread getThread() {
            return Thread.this;
        }
    }
}
