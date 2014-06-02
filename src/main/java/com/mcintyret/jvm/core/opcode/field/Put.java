package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class Put extends FieldOp {

    @Override
    public final void doExecute(Field field, OperationContext ctx) {
        WordStack stack = ctx.getStack();

        if (field.getType().getSimpleType().isDoubleWidth()) {
            int two = stack.pop();
            int one = stack.pop();
            field.set(getOop(stack), one, two);
        } else {
            int val = stack.pop();
            field.set(getOop(stack), val);
        }
    }

    protected abstract Oop getOop(WordStack stack);
}
