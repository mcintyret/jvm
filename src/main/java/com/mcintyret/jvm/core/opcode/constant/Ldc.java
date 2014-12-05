package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

class Ldc extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        int i = ctx.getConstantPool().getSingleWidth(ctx.getByteIterator().nextByteUnsigned());

        ctx.getStack().push(i);
    }

    @Override
    public byte getByte() {
        return 0x12;
    }
}
