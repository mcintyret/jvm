package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

abstract class InvokeIndirect extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShort());

        int[] values = method.newArgArray();
        int args = method.getSignature().getLength();
        for (int i = args; i >= 1; i--) {
            values[i] = ctx.getStack().pop();
        }
        values[0] = ctx.getStack().pop();

        OopClass oop = Heap.getOopClass(values[0]);

        Method implementation = getImplementationMethod(method, oop);

        if (method.hasModifier(Modifier.NATIVE)) {
            ((NativeMethod) implementation).getNativeImplementation().execute(values, ctx).applyToStack(ctx.getStack());
        } else {
            int maxLocalVars = implementation.getCode().getMaxLocals();
            if (maxLocalVars > values.length) {
                int[] tmp = new int[maxLocalVars];
                System.arraycopy(values, 0, tmp, 0, values.length);
                values = tmp;
            }

            ctx.getExecutionStack().push(
                new ExecutionStackElement(implementation, values, implementation.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }

        ctx.getByteIterator().nextShort(); // InvokeInterface has 2 extra args which can be ignored
    }

    protected abstract Method getImplementationMethod(Method method, OopClass oop);

}
