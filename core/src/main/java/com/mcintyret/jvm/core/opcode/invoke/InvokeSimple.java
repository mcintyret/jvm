package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.parse.Modifier;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
abstract class InvokeSimple extends Invoke {

    @Override
    protected final void doInvoke(Method method, OperationContext ctx) {
        Variables args = getValues(ctx, method);

        if (method.hasModifier(Modifier.NATIVE)) {
            invokeNativeMethod((NativeMethod) method, args, ctx);
        } else {
            ctx.getExecutionStack().push(
                new ExecutionStackElement(method, args, method.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }
    }

    private Variables getValues(OperationContext ctx, Method method) {
        boolean isStatic = method.isStatic();

        int shift = isStatic ? 0 : 1;

        Variables args = method.newArgArray();
        int argCount = method.getSignature().getLength();

        for (int i = argCount - (1 - shift); i >= shift; i--) {
            args.put(i, ctx.getStack().pop());
        }

        if (!isStatic) {
            args.putOop(0, ctx.getStack().popOop());
        }
        return args;
    }
}
