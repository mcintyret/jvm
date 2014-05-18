package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class L2D extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push((double) stack.popLong());
    }

    @Override
    public byte getByte() {
        return (byte) 0x8A;
    }
}

