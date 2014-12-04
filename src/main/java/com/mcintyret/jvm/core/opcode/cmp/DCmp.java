package com.mcintyret.jvm.core.opcode.cmp;

import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class DCmp extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        double b = stack.popDouble();
        double a = stack.popDouble();

        if (Double.isNaN(a) || Double.isNaN(b)) {
            stack.push(nanResult());
        } else {
            stack.push(Double.compare(a, b));
        }
    }

    protected abstract int nanResult();

}
