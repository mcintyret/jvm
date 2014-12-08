package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class FConst extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getStack().pushFloat(getConst());
    }

    protected abstract float getConst();
}
