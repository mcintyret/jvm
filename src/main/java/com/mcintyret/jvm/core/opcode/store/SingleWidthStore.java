package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class SingleWidthStore extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getVariables()[getIndex(ctx.getByteIterator())] = ctx.getStack().pop();
    }

    protected abstract int getIndex(ByteIterator bytes);
}
