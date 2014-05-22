package com.mcintyret.jvm.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MarkRef {

    private final Lock monitor = new ReentrantLock();

    public Lock getMonitor() {
        return monitor;
    }
}
