package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.WordStack;

public abstract class BinaryIntOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push(binaryOp(stack.pop(), stack.pop()));
    }

    protected abstract int binaryOp(int a, int b);

}
