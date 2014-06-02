package com.mcintyret.jvm.core.thread;

import java.util.HashMap;
import java.util.Map;

import com.mcintyret.jvm.core.oop.Oop;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Threads {

    private static final Map<Long, Thread> THREADS = new HashMap<>();

    public static Thread get(long id) {
        return THREADS.get(id);
    }

    public static Thread get(Oop oopThread) {
        return get(Thread.getThreadId(oopThread));
    }

    public static void register(Thread thread) {
        if (THREADS.put(thread.getId(), thread) != null) {
            throw new AssertionError("Multiple live threads with id " + thread.getId());
        }
    }

    public static void deregister(Thread thread) {
        if (THREADS.remove(thread.getId()) != thread) {
            throw new AssertionError("Thread with id '" + thread.getId() + "' not registered");
        }
    }

}
