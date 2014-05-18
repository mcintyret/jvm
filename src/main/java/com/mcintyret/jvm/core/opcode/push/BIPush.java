package com.mcintyret.jvm.core.opcode.push;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class BIPush extends OpCode {
    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(ctx.getByteIterator().next());
    }

    @Override
    public byte getByte() {
        return 0x10;
    }
}
