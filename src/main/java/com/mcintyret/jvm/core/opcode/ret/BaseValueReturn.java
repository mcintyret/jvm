package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.exec.ExecutionStack;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class BaseValueReturn extends BaseReturn {

    protected final void returnValue(OperationContext ctx) {
        ExecutionStack executionStack = ctx.getExecutionStack();
        ExecutionStackElement next = executionStack.peek();
        if (next == null) {
            executionStack.setFinalReturn(finalReturn(ctx.getStack()));
        } else {
            pushReturnVal(next.getStack(), ctx.getStack());
        }
    }

    protected abstract NativeReturn finalReturn(WordStack stack);

    protected abstract void pushReturnVal(WordStack lower, WordStack upper);
}
