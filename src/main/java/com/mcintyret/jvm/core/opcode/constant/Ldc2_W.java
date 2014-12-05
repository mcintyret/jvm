package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

class Ldc2_W extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        long l = ctx.getConstantPool().getDoubleWidth(ctx.getByteIterator().nextShort());

        ctx.getStack().push(l);
    }

    @Override
    public byte getByte() {
        return 0x14;
    }
}
