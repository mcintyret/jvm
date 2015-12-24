package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class LConst_1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(1L);
    }

    @Override
    public byte getByte() {
        return 0x0A;
    }
}
