package com.mcintyret.jvm.core.opcode.push;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

class SIPush extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushInt(ctx.getByteIterator().nextShortUnsigned()); // TODO: why unsigned?
    }

    @Override
    public byte getByte() {
        return 0x11;
    }
}