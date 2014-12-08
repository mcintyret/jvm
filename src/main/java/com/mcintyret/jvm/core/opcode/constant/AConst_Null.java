package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

class AConst_Null extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().pushNull();
    }

    @Override
    public byte getByte() {
        return 0x01;
    }
}
