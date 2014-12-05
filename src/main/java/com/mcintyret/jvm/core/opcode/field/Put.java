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
            field.set(getOop(stack), stack.popDoubleWidth(type));
        } else {
            field.set(getOop(stack), stack.popSingleWidth(type));
        }
    }

    protected abstract Oop getOop(VariableStack stack);
}
