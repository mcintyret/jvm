package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.parse.Modifier;

abstract class InvokeIndirect extends Invoke {

    @Override
    protected final void doInvoke(Method method, OperationContext ctx) {
        Variables args = method.newArgArray();
        int argCount = method.getSignature().getLength();
        for (int i = argCount; i >= 1; i--) {
            SimpleType type = method.getSignature().getArgTypes().get(i).asSimpleType();
            args.put(i, type, ctx.getStack().popChecked(type));
        }
        Oop oop = ctx.getStack().popOop();

        args.putOop(0, oop);

        Method implementation = getImplementationMethod(method, oop);

        if (implementation.hasModifier(Modifier.NATIVE)) {
            invokeNativeMethod((NativeMethod) method, args, ctx);
        } else {
            int maxLocalVars = implementation.getCode().getMaxLocals();
            if (maxLocalVars > args.length()) {
                int[] tmp = new int[maxLocalVars];
                System.arraycopy(args, 0, tmp, 0, args.length);
                args = tmp;
            }

            ctx.getExecutionStack().push(
                new ExecutionStackElement(implementation, args, implementation.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }

        afterInvoke(ctx);
    }

    protected void afterInvoke(OperationContext ctx) {
        // Do nothing by default
    }

    protected abstract Method getImplementationMethod(Method method, Oop oop);

}
