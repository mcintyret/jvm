package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

abstract class InvokeIndirect extends Invoke {

    @Override
    protected final void doInvoke(Method method, OperationContext ctx) {
        int[] args = method.newArgArray();
        int argCount = method.getSignature().getLength();
        for (int i = argCount; i >= 1; i--) {
            args[i] = ctx.getStack().pop();
        }
        args[0] = ctx.getStack().pop();

        Oop oop = Heap.getOop(args[0]);

        Method implementation = getImplementationMethod(method, oop);

        if (implementation.hasModifier(Modifier.NATIVE)) {
            invokeNativeMethod((NativeMethod) method, args, ctx);
        } else {
            int maxLocalVars = implementation.getCode().getMaxLocals();
            if (maxLocalVars > args.length) {
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
