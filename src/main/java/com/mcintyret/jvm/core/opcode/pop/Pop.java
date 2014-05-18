package com.mcintyret.jvm.core.opcode.pop;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class Pop extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pop();
    }

    @Override
    public byte getByte() {
        return 0x57;
    }
}
