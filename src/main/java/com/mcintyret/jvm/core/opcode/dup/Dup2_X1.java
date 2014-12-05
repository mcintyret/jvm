package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class Dup2_X1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        long top = stack.popLong();
        long next = stack.popLong();
        stack.push(top);
        stack.push(next);
        stack.push(top);
    }

    @Override
    public byte getByte() {
        return 0x5D;
    }
}
