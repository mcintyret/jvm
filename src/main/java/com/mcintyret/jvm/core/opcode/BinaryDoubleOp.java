package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;

public abstract class BinaryDoubleOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        stack.pushDouble(binaryOp(stack.popDouble(), stack.popDouble()));
    }

    protected abstract double binaryOp(double a, double b);

}
