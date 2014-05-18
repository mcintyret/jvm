package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class Ldc2_W extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        Long l = (Long) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        ctx.getStack().push(l);
    }

    @Override
    public byte getByte() {
        return 0x14;
    }
}
