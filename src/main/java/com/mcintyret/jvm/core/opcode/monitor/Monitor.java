package com.mcintyret.jvm.core.opcode.monitor;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;
import java.util.concurrent.locks.Lock;

abstract class Monitor extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        handleMonitor(Heap.getOop(ctx.getStack().pop()).getMarkRef().getMonitor());
    }

    protected abstract void handleMonitor(Lock monitor);

}
