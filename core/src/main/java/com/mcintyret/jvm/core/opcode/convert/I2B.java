package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class I2B extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push((byte) stack.pop());
    }

    @Override
    public byte getByte() {
        return (byte) 0x91;
    }
}

