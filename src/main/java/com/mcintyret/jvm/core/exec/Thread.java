package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.util.Utils;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Thread {

    private static final ClassObject THREAD_CLASS = getDefaultClassLoader().getClassObject("java/lang/Thread");

    private static final Field NAME_FIELD = THREAD_CLASS.findField("name", false);

    private static final Field ID_FIELD = THREAD_CLASS.findField("tid", false);

    private static final Method THREAD_RUN = THREAD_CLASS.findMethod("run", "()V", false);

    private final OopClass thisThread;

    private final java.lang.Thread thread;

    private volatile boolean interrupted;

    private final ExecutionStack executionStack;


    public Thread(OopClass thisThread) {
        this.executionStack = new ExecutionStack(this);
        this.thisThread = thisThread;
        this.thread = new ActualThread();
    }

    // For system threads
    public Thread(OopClass thisThread, java.lang.Thread thread) {
        Heap.register();
        this.executionStack = new ExecutionStack(this);
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

    public ExecutionStack getExecutionStack() {
        return executionStack;
    }

    private static String getThreadName(Oop thread) {
        return Utils.toString(Heap.getOopArray(thread.getFields()[NAME_FIELD.getOffset()]));
    }

    static long getThreadId(Oop thread) {
        int offset = ID_FIELD.getOffset();
        int[] fields = thread.getFields();
        return Utils.toLong(fields[offset], fields[offset + 1]);
    }

    private class ActualThread extends java.lang.Thread {

        @Override
        public void run() {
            Heap.register();
            try {
                Variables args = THREAD_RUN.newArgArray();
                args.putOop(0, thisThread);
                executionStack.push(new ExecutionStackElement(THREAD_RUN, args, THREAD_CLASS.getConstantPool(), executionStack));
                executionStack.execute();
            } finally {
                Threads.deregister(Thread.this);
                Heap.deregister();
            }
        }
    }
}
