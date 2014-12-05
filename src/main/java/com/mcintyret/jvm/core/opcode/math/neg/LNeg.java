package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

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
