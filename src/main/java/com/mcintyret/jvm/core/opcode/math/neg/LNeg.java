package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

public class LNeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(-ctx.getStack().popLong());
    }

    @Override
    public byte getByte() {
        return 0x75;
    }
}
