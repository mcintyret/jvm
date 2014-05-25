package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ByteCode;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.InterfaceMethod;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

class InvokeInterface extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {

        InterfaceMethod method = (InterfaceMethod) ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShort());

        int args = method.getSignature().getLength();
        int[] values = new int[args + 1];
        for (int i = args; i >= 1; i--) {
            values[i] = ctx.getStack().pop();
        }
        values[0] = ctx.getStack().pop();

        OopClass oop = Heap.getOopClass(values[0]);

        Method implementation = method.getMethodForImplementation(oop.getClassObject());

        int maxLocalVars = implementation.getCode().getMaxLocals();
        if (maxLocalVars > values.length) {
            int[] tmp = new int[maxLocalVars];
            System.arraycopy(values, 0, tmp, 0, values.length);
            values = tmp;
        }

        if (method.hasModifier(Modifier.NATIVE)) {
            ((NativeMethod) implementation).getNativeImplementation().execute(values).applyToStack(ctx.getStack());
        } else {
            ctx.getExecutionStack().push(
                new ExecutionStackElement(new ByteCode(implementation.getCode().getCode()), values, implementation.getClassObject().getConstantPool(), ctx.getExecutionStack()));
        }

        ctx.getByteIterator().nextShort(); // InvokeInterface has 2 extra args which can be ignored
    }

    @Override
    public byte getByte() {
        return (byte) 0xB9;
    }
}
