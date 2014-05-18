package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class Return extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getExecutionStack().pop();
    }

    @Override
    public byte getByte() {
        return (byte) 0xB1;
    }
}
