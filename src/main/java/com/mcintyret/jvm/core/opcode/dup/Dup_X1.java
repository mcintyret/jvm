package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup_X1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        Variable top = stack.pop();
        Variable next = stack.pop();

        stack.push(top);
        stack.push(next);
        stack.push(top);
    }

    @Override
    public byte getByte() {
        return 0x5A;
    }
}
