package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class InvokeSpecial extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        int opAddress = ctx.getStack().pop();

        MethodReference ref = (MethodReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        Method method = ref.getInstanceMethod();

        int[] values = new int[method.getMaxLocalVariables()];
        values[0] = opAddress;
        int args = method.getSignature().getArgTypes().size();
        for (int i = 1; i <= args; i++) {
            values[i] = ctx.getStack().pop();
        }

        ctx.getExecutionStack().push(
            new ExecutionStackElement(method.getByteCode(), values, ref.getClassObject().getConstantPool(), ctx.getExecutionStack()));
    }

    @Override
    public byte getByte() {
        return (byte) 0xB7;
    }

}