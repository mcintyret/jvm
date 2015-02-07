package com.mcintyret.jvm.core.opcode.ret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
abstract class BaseReturn extends OpCode {

    private static final Logger LOG = LoggerFactory.getLogger(BaseReturn.class);

    @Override
    public final void execute(OperationContext ctx) {
        ctx.getExecutionStack().pop();

        returnValue(ctx);

        ctx.onComplete(); // releases lock if this method was synchronized
        LOG.info("Returning from {}", ctx.getMethod());
    }

    protected abstract void returnValue(OperationContext ctx);

}
