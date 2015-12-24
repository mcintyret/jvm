package com.mcintyret.jvm.core.opcode.cmp;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class DCmp extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        double b = stack.popDouble();
        double a = stack.popDouble();

        if (Double.isNaN(a) || Double.isNaN(b)) {
            stack.pushInt(nanResult());
        } else {
            stack.pushInt(Double.compare(a, b));
        }
    }

    protected abstract int nanResult();

}
