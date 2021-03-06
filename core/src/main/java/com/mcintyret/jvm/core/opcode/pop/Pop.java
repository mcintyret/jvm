package com.mcintyret.jvm.core.opcode.pop;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

public class Pop extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().popRaw();
    }

    @Override
    public byte getByte() {
        return 0x57;
    }
}
