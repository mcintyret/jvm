package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class DoubleWidthLoad extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        ctx.getStack().push(ctx.getLocalVars()[index]);
        ctx.getStack().push(ctx.getLocalVars()[index + 1]);
    }

    protected abstract int getIndex(ByteIterator bytes);
}
