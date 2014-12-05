package com.mcintyret.jvm.core.opcode.cmp;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class FCmp extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        float b = stack.popFloat();
        float a = stack.popFloat();

        if (Float.isNaN(a) || Float.isNaN(b)) {
            stack.push(nanResult());
        } else {
            stack.push(Float.compare(a, b));
        }
    }

    protected abstract int nanResult();

}
