package com.mcintyret.jvm.core.opcode.invoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class Invoke extends OpCode {

    private static final Logger LOG = LoggerFactory.getLogger(Invoke.class);

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShortUnsigned());
        LOG.info("Invoking {}.{}", method.getClassObject().getType().getClassName(), method.getSignature());

        doInvoke(method, ctx);
    }

    protected abstract void doInvoke(Method method, OperationContext ctx);

}
