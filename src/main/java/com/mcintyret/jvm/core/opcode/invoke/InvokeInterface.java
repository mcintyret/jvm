package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.NativeMethod;
import com.mcintyret.jvm.core.Oop;
import com.mcintyret.jvm.core.constantpool.InterfaceMethodReference;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

class InvokeInterface extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {

        MethodReference ref = (MethodReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());
        Method method = ref.getMethod();

        int args = method.getSignature().getArgTypes().size();
        int[] values = new int[args + 1];
        for (int i = args; i >= 1; i--) {
            values[i] = ctx.getStack().pop();
        }
        values[0] = ctx.getStack().pop();

        InterfaceMethodReference imr = (InterfaceMethodReference) method.getMethodReference();
        Oop oop = Heap.getOop(values[0]);

        method = imr.getMethodForImplementation(oop.getClassObject().getType().getClassName());

        int maxLocalVars = method.getMaxLocalVariables();
        if (maxLocalVars > values.length) {
            int[] tmp = new int[maxLocalVars];
            System.arraycopy(values, 0, tmp, 0, values.length);
            values = tmp;
        }

        if (method.hasModifier(Modifier.NATIVE)) {
            ((NativeMethod) method).getNativeExecution().execute(values).applyToStack(ctx.getStack());
        } else {
            ctx.getExecutionStack().push(
                new ExecutionStackElement(method.getByteCode(), values, oop.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }

        ctx.getByteIterator().nextShort(); // InvokeInterface has 2 extra args which can be ignored
    }

    @Override
    public byte getByte() {
        return (byte) 0xB9;
    }
}
