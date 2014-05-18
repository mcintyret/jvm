package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.WordStack;

public abstract class BinaryLongOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push(binaryOp(stack.popLong(), stack.popLong()));
    }

    protected abstract long binaryOp(long a, long b);

}
