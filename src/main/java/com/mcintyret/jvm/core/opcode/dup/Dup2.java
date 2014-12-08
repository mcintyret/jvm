package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        WideVariable v = stack.popWide();

        stack.pushWide(v);
        stack.pushWide(v);
    }

    @Override
    public byte getByte() {
        return 0x5C;
    }
}
