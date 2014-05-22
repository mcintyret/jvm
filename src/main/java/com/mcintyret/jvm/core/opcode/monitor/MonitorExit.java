package com.mcintyret.jvm.core.opcode.monitor;

import java.util.concurrent.locks.Lock;

class MonitorExit extends Monitor {

    @Override
    protected void handleMonitor(Lock monitor) {
        monitor.unlock();
    }

    @Override
    public byte getByte() {
        return (byte) 0xC3;
    }
}
