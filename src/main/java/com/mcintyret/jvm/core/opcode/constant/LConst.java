package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class LConst extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getStack().pushLong(getConst());
    }

    protected abstract long getConst();

}
