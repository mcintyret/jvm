package com.mcintyret.jvm.core.opcode.monitor;

import java.util.concurrent.locks.Lock;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class Monitor extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        handleMonitor(ctx.getStack().popOop().getMarkRef().getMonitor());
    }

    protected abstract void handleMonitor(Lock monitor);

}
