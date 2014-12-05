package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

class I2S extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.pushShort((short) stack.popInt());
    }

    @Override
    public byte getByte() {
        return (byte) 0x93;
    }
}

