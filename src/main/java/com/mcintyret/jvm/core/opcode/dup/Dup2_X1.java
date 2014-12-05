package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup2_X1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        WideVariable top = stack.popWide();
        WideVariable next = stack.popWide();

        stack.pushWide(top);
        stack.pushWide(next);
        stack.pushWide(top);
    }

    @Override
    public byte getByte() {
        return 0x5D;
    }
}
