package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

public class FNeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushFloat(-ctx.getStack().popFloat());
    }

    @Override
    public byte getByte() {
        return 0x76;
    }
}
