package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
abstract class BaseReturn extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getExecutionStack().pop();

        returnValue(ctx);

        System.out.println("Returning from " + ctx.getMethod());
    }

    protected abstract void returnValue(OperationContext ctx);

}
