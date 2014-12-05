package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class Dup2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        long l = stack.popLong();
        stack.push(l);
        stack.push(l);
    }

    @Override
    public byte getByte() {
        return 0x5C;
    }
}
