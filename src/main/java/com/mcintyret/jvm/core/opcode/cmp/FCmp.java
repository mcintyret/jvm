package com.mcintyret.jvm.core.opcode.cmp;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStackImpl;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class FCmp extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStackImpl stack = ctx.getStack();
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
