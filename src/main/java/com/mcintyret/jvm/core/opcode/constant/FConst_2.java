package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class FConst_2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(2F);
    }

    @Override
    public byte getByte() {
        return 0x0D;
    }
}
