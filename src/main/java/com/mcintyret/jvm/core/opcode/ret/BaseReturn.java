package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
abstract class BaseReturn extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ExecutionStack executionStack = ctx.getExecutionStack();
        executionStack.pop();

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
