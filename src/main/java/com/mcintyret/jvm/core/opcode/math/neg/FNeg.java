package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class FNeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(-ctx.getStack().popFloat());
    }

    @Override
    public byte getByte() {
        return 0x76;
    }
}
