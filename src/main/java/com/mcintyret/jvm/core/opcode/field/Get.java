package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class Get extends FieldOp {

    @Override
    public final void doExecute(Field field, OperationContext ctx) {
        field.get(getOop(ctx), ctx.getStack());
    }

    protected abstract Oop getOop(OperationContext ctx);

}
