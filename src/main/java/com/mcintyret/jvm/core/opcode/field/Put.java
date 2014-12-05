package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.VariableStackImpl;
import com.mcintyret.jvm.core.oop.Oop;

abstract class Put extends FieldOp {

    @Override
    public final void doExecute(Field field, OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        if (field.getType().isDoubleWidth()) {
            int two = stack.pop();
            int one = stack.pop();
            field.set(getOop(stack), one, two);
        } else {
            int val = stack.pop();
            field.set(getOop(stack), val);
        }
    }

    protected abstract Oop getOop(VariableStack stack);
}
