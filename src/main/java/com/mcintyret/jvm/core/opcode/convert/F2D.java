package com.mcintyret.jvm.core.opcode.convert;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

class F2D extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.pushDouble((double) stack.popFloat());
    }

    @Override
    public byte getByte() {
        return (byte) 0x8D;
    }
}
