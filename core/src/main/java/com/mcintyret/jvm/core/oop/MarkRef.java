package com.mcintyret.jvm.core.oop;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MarkRef {

    private final Lock monitor = new ReentrantLock();

    private final Condition monitorCondition = monitor.newCondition();

    private boolean live = false; // Only used during GC - meaningless otherwise

    public Lock getMonitor() {
        return monitor;
    }

    public Condition getMonitorCondition() {
        return monitorCondition;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
