package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class I2D extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.push((double) stack.pop());
    }

    @Override
    public byte getByte() {
        return (byte) 0x87;
    }
}

