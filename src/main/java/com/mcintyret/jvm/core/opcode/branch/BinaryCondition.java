package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class BinaryCondition extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        int jump = ctx.getByteIterator().nextShort();
        if (conditionMet(ctx.getStack().pop(), ctx.getStack().pop())) {
            ctx.getByteIterator().seek(jump - 3);
        }
    }

    protected abstract boolean conditionMet(int a, int b);
}
