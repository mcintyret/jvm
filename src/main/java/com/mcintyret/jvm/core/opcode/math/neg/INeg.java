package com.mcintyret.jvm.core.opcode.math.neg;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

public class INeg extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushInt(-ctx.getStack().popInt());
    }

    @Override
    public byte getByte() {
        return 0x74;
    }
}
