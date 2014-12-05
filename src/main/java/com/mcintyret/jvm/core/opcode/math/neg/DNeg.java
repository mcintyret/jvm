package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

public class DNeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushDouble(-ctx.getStack().popDouble());
    }

    @Override
    public byte getByte() {
        return 0x77;
    }
}
