package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;


class InvokeStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        MethodReference ref = (MethodReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        Method method = ref.getStaticMethod();

        int[] values = new int[method.getMethodSignature().getArgTypes().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = ctx.getStack().pop();
        }

        ctx.getExecutionStack().push(
            new ExecutionStackElement(method.getByteCode(), values, ref.getClassObject().getConstantPool(), ctx.getExecutionStack()));
    }

    @Override
    public byte getByte() {
        return (byte) 0xB8;
    }
}
