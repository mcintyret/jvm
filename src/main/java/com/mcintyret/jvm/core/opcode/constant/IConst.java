package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class IConst extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getStack().pushInt(getConst());
    }

    protected abstract int getConst();

}
