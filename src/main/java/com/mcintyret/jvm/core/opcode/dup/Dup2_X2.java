package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class Dup2_X2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        long one = stack.popLong();
        long two = stack.popLong();
        long three = stack.popLong();
        stack.push(one);
        stack.push(three);
        stack.push(two);
        stack.push(one);
    }

    @Override
    public byte getByte() {
        return 0x5E;
    }
}
