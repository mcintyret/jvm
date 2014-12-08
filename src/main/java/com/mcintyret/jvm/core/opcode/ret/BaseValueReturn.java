package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.exec.ExecutionStack;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.opcode.Typed;

abstract class BaseValueReturn extends BaseReturn implements Typed {

    protected final void returnValue(OperationContext ctx) {
        ExecutionStack executionStack = ctx.getExecutionStack();
        ExecutionStackElement next = executionStack.peek();
        if (next == null) {
            executionStack.setFinalReturn(finalReturn(ctx.getStack()));
        } else {
            pushReturnVal(next.getStack(), ctx.getStack());
        }
    }

    protected abstract NativeReturn finalReturn(VariableStack stack);

    protected abstract void pushReturnVal(VariableStack lower, VariableStack upper);
}
