package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class L2F extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push((float) stack.popLong());
    }

    @Override
    public byte getByte() {
        return (byte) 0x89;
    }
}
