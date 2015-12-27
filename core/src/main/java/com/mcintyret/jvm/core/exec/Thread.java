package com.mcintyret.jvm.core.exec;

import com.google.common.collect.Iterables;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.util.Utils;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Thread {

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
            t.printStackTrace();
            System.out.println("Caused By:");
            executionStacks.descendingIterator().forEachRemaining(stack -> {
                stack.getStack().descendingIterator().forEachRemaining(exec -> {
                    System.out.println(exec.getMethod());
                });
            });

            throw t;
        }
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

        private final ClassObject THREAD_CLASS = getDefaultClassLoader().getClassObject("java/lang/Thread");

        private final Method THREAD_RUN = THREAD_CLASS.findMethod("run", "()V", false);

        public ActualThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Heap.register();
            try {
                Variables args = THREAD_RUN.newArgArray();
                args.putOop(0, thisThread);

                execute(THREAD_RUN, args);

            } finally {
                Threads.deregister(Thread.this);
                Heap.deregister();
            }
        }

        public Thread getThread() {
            return Thread.this;
        }
    }
}
