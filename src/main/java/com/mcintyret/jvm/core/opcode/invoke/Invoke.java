package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class Invoke extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShortUnsigned());
        System.out.println("Invoking " + method.getClassObject().getType().getClassName() + "." + method.getSignature());

       doInvoke(method, ctx);
    }

    protected abstract void doInvoke(Method method, OperationContext ctx);

}
