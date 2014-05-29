package com.mcintyret.jvm.core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MarkRef {

    private final Lock monitor = new ReentrantLock();

    private final Condition monitorCondition = monitor.newCondition();

    public Lock getMonitor() {
        return monitor;
    }

    public Condition getMonitorCondition() {
        return monitorCondition;
    }
}
