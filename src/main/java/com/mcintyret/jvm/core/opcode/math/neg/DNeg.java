package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

public class DNeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(-ctx.getStack().popDouble());
    }

    @Override
    public byte getByte() {
        return 0x77;
    }
}
