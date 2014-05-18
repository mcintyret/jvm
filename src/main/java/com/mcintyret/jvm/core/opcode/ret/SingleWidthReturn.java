package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class SingleWidthReturn extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ExecutionStack executionStack = ctx.getExecutionStack();
        executionStack.pop();
        executionStack.peek().getStack().push(ctx.getStack().pop());
    }
}
