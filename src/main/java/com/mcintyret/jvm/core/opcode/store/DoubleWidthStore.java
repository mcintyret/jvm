package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class DoubleWidthStore extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        ctx.getLocalVars()[index+1] = ctx.getStack().pop();
        ctx.getLocalVars()[index] = ctx.getStack().pop();
    }

    protected abstract int getIndex(ByteIterator bytes);
}
