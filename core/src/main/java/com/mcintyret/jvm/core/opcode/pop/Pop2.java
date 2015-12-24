package com.mcintyret.jvm.core.opcode.pop;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

public class Pop2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().popLong();
    }

    @Override
    public byte getByte() {
        return 0x58;
    }
}
