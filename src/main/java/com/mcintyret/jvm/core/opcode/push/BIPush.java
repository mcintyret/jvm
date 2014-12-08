package com.mcintyret.jvm.core.opcode.push;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

class BIPush extends OpCode {
    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushByte(ctx.getByteIterator().nextByte());
    }

    @Override
    public byte getByte() {
        return 0x10;
    }
}
