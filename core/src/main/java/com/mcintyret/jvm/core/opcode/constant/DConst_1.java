package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class DConst_1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(1D);
    }

    @Override
    public byte getByte() {
        return 0x0F;
    }
}
