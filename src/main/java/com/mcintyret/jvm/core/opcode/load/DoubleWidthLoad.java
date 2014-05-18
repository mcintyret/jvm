package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class DoubleWidthLoad extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        ctx.getStack().push(ctx.getVariables()[index]);
        ctx.getStack().push(ctx.getVariables()[index + 1]);
    }

    protected abstract int getIndex(ByteIterator bytes);
}
