package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class SingleWidthLoad extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getStack().push(ctx.getVariables()[getIndex(ctx.getByteIterator())]);
    }

    protected abstract int getIndex(ByteIterator bytes);
}
