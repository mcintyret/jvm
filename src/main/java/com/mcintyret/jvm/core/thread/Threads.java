package com.mcintyret.jvm.core.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Threads {

    private static final Map<String, Thread> THREADS = new HashMap<>();

    public static Thread get(String name) {
        return THREADS.get(name);
    }

    public static void register(Thread thread) {
        THREADS.put(thread.getName(), thread);
    }

}
