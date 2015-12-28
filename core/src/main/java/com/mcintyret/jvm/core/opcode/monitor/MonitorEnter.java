package com.mcintyret.jvm.core.opcode.monitor;

import com.mcintyret.jvm.core.Heap;

import java.util.concurrent.locks.Lock;

class MonitorEnter extends Monitor {

    @Override
    protected void handleMonitor(Lock monitor) {
        if (!monitor.tryLock()) {
            Heap.threadSleeping();
            monitor.lock();
            Heap.threadWaking();
        }
    }

    @Override
    public byte getByte() {
        return (byte) 0xC2;
    }
}
