package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.NativeMethod;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
abstract class Invoke extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        boolean isStatic = isStatic();
        int shift = isStatic ? 0 : 1;

        MethodReference ref = ctx.getConstantPool().getMethodReference(ctx.getByteIterator().nextShort());
        Method method = ref.getMethod();

        int args = method.getSignature().getLength();
        int[] values = new int[Math.max(args, method.getMaxLocalVariables() + shift)];
        for (int i = args - (1 - shift); i >= shift; i--) {
            values[i] = ctx.getStack().pop();
        }
        if (!isStatic) {
            values[0] = ctx.getStack().pop();
        }

        if (method.hasModifier(Modifier.NATIVE)) {
            ((NativeMethod) method).getNativeExecution().execute(values).applyToStack(ctx.getStack());
        } else {
            ctx.getExecutionStack().push(
                    new ExecutionStackElement(method.getByteCode(), values, ref.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }
    }

    protected boolean isStatic() {
        // Common case
        return false;
    }


}
