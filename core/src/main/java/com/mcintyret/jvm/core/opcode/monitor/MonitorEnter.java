package com.mcintyret.jvm.core.opcode.monitor;

import java.util.concurrent.locks.Lock;

class MonitorEnter extends Monitor {

    @Override
    protected void handleMonitor(Lock monitor) {
        monitor.lock();
    }

    @Override
    public byte getByte() {
        return (byte) 0xC2;
    }
}
