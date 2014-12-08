package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class Put extends FieldOp {

    @Override
    public final void doExecute(Field field, OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        SimpleType type = field.getType().asSimpleType();

        if (field.getType().isDoubleWidth()) {
            int two = stack.popSingleWidth(type);
            int one = stack.popSingleWidth(type);
            field.set(getOop(stack), one, two);
        } else {
            int val = stack.popSingleWidth(type);
            field.set(getOop(stack), val);
        }
    }

    protected abstract Oop getOop(VariableStack stack);
}
