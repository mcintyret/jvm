package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;

public abstract class BinaryFloatOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.push(binaryOp(stack.popFloat(), stack.popFloat()));
    }

    protected abstract float binaryOp(float a, float b);


}
