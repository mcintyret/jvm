package com.mcintyret.jvm.core.thread;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.oop.OopClass;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Thread {

    private static final ClassObject THREAD_CLASS = getDefaultClassLoader().getClassObject("java/lang/Thread");

    private static final Method THREAD_RUN = THREAD_CLASS.findMethod("run", "()V", false);

    private final OopClass thisThread;

    private final String name;

    private java.lang.Thread thread;

    public Thread(OopClass thisThread, String name) {
        this.thisThread = thisThread;
        this.name = name;
        this.thread = new ActualThread();
    }

    // For system threads
    public Thread(OopClass thisThread, String name, java.lang.Thread thread) {
        this.thisThread = thisThread;
        this.name = name;
        this.thread = thread;
    }

    public void start() {
        thread.start();
    }

    public OopClass getThisThread() {
        return thisThread;
    }

    public String getName() {
        return name;
    }

    public void interrupt() {
        thread.interrupt();
    }

    private class ActualThread extends java.lang.Thread {

        private final ExecutionStack executionStack = new ExecutionStack(Thread.this);

        @Override
        public void run() {
            try {
                int[] args = THREAD_RUN.newArgArray();
                args[0] = thisThread.getAddress();
                executionStack.push(new ExecutionStackElement(THREAD_RUN, args, THREAD_CLASS.getConstantPool(), executionStack));
                executionStack.execute();
            } finally {
                Threads.deregister(Thread.this);
            }
        }
    }
}
