package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class Dup extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.push(stack.peek());
    }

    @Override
    public byte getByte() {
        return 0x59;
    }
}
