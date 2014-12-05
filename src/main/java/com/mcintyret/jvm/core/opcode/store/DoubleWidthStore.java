package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.util.ByteIterator;

abstract class DoubleWidthStore extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        ctx.getLocalVariables()[index+1] = ctx.getStack().pop();
        ctx.getLocalVariables()[index] = ctx.getStack().pop();
    }

    protected abstract int getIndex(ByteIterator bytes);
}
