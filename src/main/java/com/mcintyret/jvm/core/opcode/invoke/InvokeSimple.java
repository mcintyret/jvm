package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
abstract class InvokeSimple extends Invoke {

    @Override
    protected final void doInvoke(Method method, OperationContext ctx) {
        int[] values = getValues(ctx, method);

        if (method.hasModifier(Modifier.NATIVE)) {
            invokeNativeMethod((NativeMethod) method, values, ctx);
        } else {
            ctx.getExecutionStack().push(
                new ExecutionStackElement(method, values, method.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }
    }


    private int[] getValues(OperationContext ctx, Method method) {
        boolean isStatic = method.isStatic();

        int shift = isStatic ? 0 : 1;

        int[] values = method.newArgArray();
        int args = method.getSignature().getLength();

        for (int i = args - (1 - shift); i >= shift; i--) {
            values[i] = ctx.getStack().pop();
        }

        if (!isStatic) {
            values[0] = ctx.getStack().pop();
        }
        return values;
    }
}
